import unittest
from oeqa.oetest import oeRuntimeTest
from oeqa.utils.decorators import *

def setUpModule():
    if not oeRuntimeTest.hasFeature("package-management"):
        skipModule("Image doesn't have package management feature")
    if not oeRuntimeTest.hasPackage("smart"):
        skipModule("Image doesn't have smart installed")

class SmartHelpTest(oeRuntimeTest):

    def test_smart_help(self):
        status = self.target.run('smart --help')[0]
        self.assertEqual(status, 0)

class SmartQueryTest(oeRuntimeTest):

    @skipUnlessPassed('test_smart_help')
    def test_smart_query(self):
        (status, output) = self.target.run('smart query rpm')
        self.assertEqual(status, 0, msg="smart query failed, output: %s" % output)

    @skipUnlessPassed('test_smart_query')
    def test_smart_info(self):
        (status, output) = self.target.run('smart info rpm')
        self.assertEqual(status, 0, msg="smart info rpm failed, output: %s" % output)
