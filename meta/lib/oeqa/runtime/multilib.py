import unittest
from oeqa.oetest import oeRuntimeTest, skipModule

def setUpModule():
    multilibs = oeRuntimeTest.tc.d.getVar("MULTILIBS", True) or ""
    if "multlib:lib32" not in multilibs:
        skipModule("this isn't a multilib:lib32 image")


class MultilibFileTest(oeRuntimeTest):

    def test_file_connman(self):
        (status, output) = self.target.run('file -L /usr/sbin/connmand | grep "ELF 32-bit LSB executable"')
        self.assertEqual(status, 0, msg="status and output : %s and %s" % (status,output))
