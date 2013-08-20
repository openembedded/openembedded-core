import subprocess
import unittest
import os
from oeqa.oetest import oeRuntimeTest
from oeqa.utils.decorators import *

def setUpModule():
    if not (oeRuntimeTest.hasPackage("dropbear") or oeRuntimeTest.hasPackage("openssh-sshd")):
        skipModule("No ssh package in image")


class ScpTest(oeRuntimeTest):

    def setUp(self):
         subprocess.check_call("dd if=/dev/zero of=%s bs=512k count=10" % os.path.join(oeRuntimeTest.tc.d.getVar("TEST_LOG_DIR", True), 'test_scp_file'), shell=True)

    @skipUnlessPassed('test_ssh')
    def test_scp(self):
        (status, output) = self.target.copy_to(os.path.join(oeRuntimeTest.tc.d.getVar("TEST_LOG_DIR", True), 'test_scp_file'), '/tmp/test_scp_file')
        self.assertEqual(status, 0, msg = "File could not be copied. Output: %s" % output)
        (status, output) = self.target.run("ls -la /tmp/test_scp_file")
        self.assertEqual(status, 0, msg = "SCP test failed")
