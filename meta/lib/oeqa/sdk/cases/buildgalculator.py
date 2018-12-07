import os
import subprocess
import tempfile
import unittest

import bb

from oeqa.sdk.case import OESDKTestCase
from oeqa.sdk.utils.sdkbuildproject import SDKBuildProject

from oeqa.utils.subprocesstweak import errors_have_output
errors_have_output()

class GalculatorTest(OESDKTestCase):
    td_vars = ['DATETIME']

    def setUp(self):
        if not (self.tc.hasTargetPackage("gtk+3", multilib=True) or \
                self.tc.hasTargetPackage("libgtk-3.0", multilib=True)):
            raise unittest.SkipTest("GalculatorTest class: SDK don't support gtk+3")
        if not (self.tc.hasHostPackage("nativesdk-gettext-dev")):
            raise unittest.SkipTest("GalculatorTest class: SDK doesn't contain gettext")

    def test_galculator(self):
        with tempfile.TemporaryDirectory(prefix="galculator", dir=self.tc.sdk_dir) as testdir:
            dl_dir = self.td.get('DL_DIR', None)
            tarball = self.fetch(testdir, dl_dir, "http://galculator.mnim.org/downloads/galculator-2.1.4.tar.bz2")

            dirs = {}
            dirs["source"] = os.path.join(testdir, "galculator-2.1.4")
            dirs["build"] = os.path.join(testdir, "build")
            dirs["install"] = os.path.join(testdir, "install")

            subprocess.check_output(["tar", "xf", tarball, "-C", testdir])
            self.assertTrue(os.path.isdir(dirs["source"]))

            bb.utils.mkdirhier(dirs["build"])
            self._run("cd {source} && autoreconf -i -f -I $OECORE_TARGET_SYSROOT/usr/share/aclocal -I m4".format(**dirs))
            self._run("cd {build} && {source}/configure $CONFIGURE_FLAGS".format(**dirs))
            self._run("cd {build} && make -j".format(**dirs))
            self._run("cd {build} && make install DESTDIR={install}".format(**dirs))

            self.check_elf(os.path.join(dirs["install"], "usr", "local", "bin", "galculator"))
