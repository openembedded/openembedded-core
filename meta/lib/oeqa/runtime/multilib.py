import unittest
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

def setUpModule():
    multilibs = oeRuntimeTest.tc.d.getVar("MULTILIBS", True) or ""
    if "multilib:lib32" not in multilibs:
        skipModule("this isn't a multilib:lib32 image")


class MultilibTest(oeRuntimeTest):

    @testcase('279')
    @skipUnlessPassed('test_ssh')
    def test_file_connman(self):
        self.assertTrue(oeRuntimeTest.hasPackage('connman-gnome'), msg="This test assumes connman-gnome is installed")
        (status, output) = self.target.run("readelf -h /usr/bin/connman-applet | sed -n '3p' | awk '{print $2}'")
        self.assertEqual(output, "ELF32", msg="connman-applet isn't an ELF32 binary. readelf says: %s" % self.target.run("readelf -h /usr/bin/connman-applet")[1])
