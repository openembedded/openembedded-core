#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.runtime.decorator.package import OEHasPackage

class RustCompileTest(OERuntimeTestCase):

    @classmethod
    def setUp(cls):
        dst = '/tmp/'
        src = os.path.join(cls.tc.files_dir, 'test.rs')
        cls.tc.target.copyTo(src, dst)

    @classmethod
    def tearDown(cls):
        files = '/tmp/test.rs /tmp/test'
        cls.tc.target.run('rm %s' % files)

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['rust'])
    def test_rust_compile(self):
        status, output = self.target.run('rustc /tmp/test.rs -o /tmp/test')
        msg = 'rust compile failed, output: %s' % output
        self.assertEqual(status, 0, msg=msg)

        status, output = self.target.run('/tmp/test')
        msg = 'running compiled file failed, output: %s' % output
        self.assertEqual(status, 0, msg=msg)

class RustHelloworldTest(OERuntimeTestCase):
    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(['rust-hello-world'])
    def test_rusthelloworld(self):
        cmd = "rust-hello-world"
        status, output = self.target.run(cmd)
        msg = 'Exit status was not 0. Output: %s' % output
        self.assertEqual(status, 0, msg=msg)

        msg = 'Incorrect output: %s' % output
        self.assertEqual(output, "Hello, world!", msg=msg)
