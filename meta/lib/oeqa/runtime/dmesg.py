import unittest
from oeqa.oetest import oeRuntimeTest
from oeqa.utils.decorators import *

def setUpModule():
    skipModuleUnless(oeRuntimeTest.tc.target.run('which dmesg')[0] == 0, "No dmesg in image or no connection")

class DmesgTest(oeRuntimeTest):

    @skipUnlessPassed('test_ssh')
    def test_dmesg(self):
        (status, output) = self.target.run('dmesg | grep -v mmci-pl18x | grep -i error')
        self.assertEqual(status, 1, msg = "Error messages in dmesg log: %s" % output)
