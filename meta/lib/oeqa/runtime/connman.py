import unittest
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

def setUpModule():
    if not oeRuntimeTest.hasPackage("connman"):
        skipModule("No connman package in image")


class ConnmanTest(oeRuntimeTest):

    @skipUnlessPassed('test_ssh')
    def test_connmand_help(self):
        (status, output) = self.target.run('/usr/sbin/connmand --help')
        self.assertEqual(status, 0, msg="status and output: %s and %s" % (status,output))


    @skipUnlessPassed('test_connmand_help')
    def test_connmand_running(self):
        (status, output) = self.target.run(oeRuntimeTest.pscmd + ' | grep [c]onnmand')
        self.assertEqual(status, 0, msg="no connmand process, ps output: %s" % self.target.run(oeRuntimeTest.pscmd)[1])

    @skipUnlessPassed('test_connmand_running')
    def test_connmand_unique(self):
        self.target.run('/usr/sbin/connmand')
        output = self.target.run(oeRuntimeTest.pscmd + ' | grep -c [c]onnmand')[1]
        self.assertEqual(output, "1", msg="more than one connmand running in background, ps output: %s\n%s" % (output, self.target.run(oeRuntimeTest.pscmd)[1]))


