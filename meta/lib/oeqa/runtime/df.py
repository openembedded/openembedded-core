import unittest
from oeqa.oetest import oeRuntimeTest
from oeqa.utils.decorators import *

def setUpModule():
    skipModuleUnless(oeRuntimeTest.tc.target.run('which df')[0] == 0, "No df in image or no connection")

class DfTest(oeRuntimeTest):

    @skipUnlessPassed("test_ssh")
    def test_df(self):
        (status,output) = self.target.run("df / | sed -n '2p' | awk '{print $4}'")
        self.assertTrue(int(output)>5120, msg="Not enough space on image. Current size is %s" % output)
