import subprocess
import unittest
import sys
import time
from oeqa.oetest import oeRuntimeTest

class PingTest(oeRuntimeTest):

    def test_ping(self):
        output = ''
        status = None
        endtime = time.time() + 30
        while status != 0 and time.time() < endtime:
            proc = subprocess.Popen("ping -c 1 %s" % oeRuntimeTest.tc.qemu.ip, shell=True, stdout=subprocess.PIPE)
            output += proc.communicate()[0]
            status = proc.poll()
        self.assertEqual(status, 0, msg = "No echo reply, ping output is:\n%s" % output)
