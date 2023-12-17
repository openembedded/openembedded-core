#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import unittest
from oeqa.sdk.case import OESDKTestCase

from oeqa.utils.subprocesstweak import errors_have_output

errors_have_output()


class MaturinTest(OESDKTestCase):
    def setUp(self):
        if not (
            self.tc.hasHostPackage("nativesdk-python3-maturin")
            or self.tc.hasHostPackage("python3-maturin-native")
        ):
            raise unittest.SkipTest("No python3-maturin package in the SDK")

    def test_maturin_list_python(self):
        py_major = self._run("python3 -c 'import sys; print(sys.version_info.major)'")
        py_minor = self._run("python3 -c 'import sys; print(sys.version_info.minor)'")
        python_version = "%s.%s" % (py_major.strip(), py_minor.strip())
        cmd = "maturin list-python"
        output = self._run(cmd)
        self.assertRegex(output, r"^🐍 1 python interpreter found:\n")
        self.assertRegex(
            output,
            r" - CPython %s (.+)/usr/bin/python%s$" % (python_version, python_version),
        )
