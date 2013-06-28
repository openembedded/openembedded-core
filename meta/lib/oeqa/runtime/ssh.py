import subprocess
import unittest
import sys
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

def setUpModule():
    if not (oeRuntimeTest.hasPackage("dropbear") or oeRuntimeTest.hasPackage("openssh")):
        skipModule("No ssh package in image")

class SshTest(oeRuntimeTest):

    @skipUnlessPassed('test_ping')
    def test_ssh(self):
        (status, output) = self.target.run('uname -a')
        self.assertEqual(status, 0, msg="SSH Test failed: %s" % output)
