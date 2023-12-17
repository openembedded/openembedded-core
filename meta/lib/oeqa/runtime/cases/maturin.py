#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import os

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.runtime.decorator.package import OEHasPackage

class MaturinTest(OERuntimeTestCase):
    @OETestDepends(['ssh.SSHTest.test_ssh', 'python.PythonTest.test_python3'])
    @OEHasPackage(['python3-maturin'])
    def test_maturin_list_python(self):
        status, output = self.target.run("maturin list-python")
        self.assertEqual(status, 0)
        _, py_major = self.target.run("python3 -c 'import sys; print(sys.version_info.major)'")
        _, py_minor = self.target.run("python3 -c 'import sys; print(sys.version_info.minor)'")
        python_version = "%s.%s" % (py_major, py_minor)
        self.assertEqual(output, "🐍 1 python interpreter found:\n"
                                 " - CPython %s at /usr/bin/python%s" % (python_version, python_version))
