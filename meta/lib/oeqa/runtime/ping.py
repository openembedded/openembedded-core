import subprocess
import unittest
import sys
from oeqa.oetest import oeRuntimeTest

class PingTest(oeRuntimeTest):

    def test_ping(self):
        status = subprocess.call("ping -w 30 -c 1 %s" % oeRuntimeTest.tc.qemu.ip, shell=True, stdout=subprocess.PIPE)
        self.assertEqual(status, 0)

