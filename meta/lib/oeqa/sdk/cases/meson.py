#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import json
import os
import subprocess
import tempfile
import unittest

from oeqa.sdk.case import OESDKTestCase
from oeqa.utils.subprocesstweak import errors_have_output
errors_have_output()

class MesonTest(OESDKTestCase):
    """
    Test that Meson builds correctly.
    """
    def setUp(self):
        libc = self.td.get("TCLIBC")
        if libc in [ 'newlib' ]:
            raise unittest.SkipTest("MesonTest class: SDK doesn't contain a supported C library")

        if not (self.tc.hasHostPackage("nativesdk-meson") or
                self.tc.hasHostPackage("meson-native")):
            raise unittest.SkipTest("MesonTest: needs meson")

    def test_epoxy(self):
        with tempfile.TemporaryDirectory(prefix="epoxy", dir=self.tc.sdk_dir) as testdir:
            tarball = self.fetch(testdir, self.td["DL_DIR"], "https://github.com/anholt/libepoxy/releases/download/1.5.3/libepoxy-1.5.3.tar.xz")

            dirs = {}
            dirs["source"] = os.path.join(testdir, "libepoxy-1.5.3")
            dirs["build"] = os.path.join(testdir, "build")
            dirs["install"] = os.path.join(testdir, "install")

            subprocess.check_output(["tar", "xf", tarball, "-C", testdir], stderr=subprocess.STDOUT)
            self.assertTrue(os.path.isdir(dirs["source"]))
            os.makedirs(dirs["build"])

            log = self._run("meson setup --warnlevel 1 -Degl=no -Dglx=no -Dx11=false {build} {source}".format(**dirs))

            # Check that the host (gcc) and build (cross-gcc) compilers are different
            data = json.loads(self._run("meson introspect --compilers {build}".format(**dirs)))
            self.assertNotEqual(data["build"]["c"]["exelist"], data["host"]["c"]["exelist"])
            # Check that the system architectures were set correctly
            data = json.loads(self._run("meson introspect --machines {build}".format(**dirs)))
            self.assertEqual(data["build"]["cpu"], self.td["SDK_ARCH"])
            self.assertEqual(data["host"]["cpu"], self.td["HOST_ARCH"])

            self._run("meson compile -C {build} -v".format(**dirs))
            self._run("meson install -C {build} --destdir {install}".format(**dirs))

            self.check_elf(os.path.join(dirs["install"], "usr", "local", "lib", "libepoxy.so"))
