import unittest
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

def setUpModule():
    if not oeRuntimeTest.hasFeature("package-management"):
            skipModule("rpm module skipped: target doesn't have package-management in IMAGE_FEATURES")
    if "package_rpm" != oeRuntimeTest.tc.d.getVar("PACKAGE_CLASSES", True).split()[0]:
            skipModule("rpm module skipped: target doesn't have rpm as primary package manager")


class RpmHelpTest(oeRuntimeTest):

    @skipUnlessPassed('test_ssh')
    def test_rpm_help(self):
        (status, output) = self.target.run('rpm --help')
        self.assertEqual(status, 0, msg="status and output: %s and %s" % (status,output))

class RpmQueryTest(oeRuntimeTest):

    @skipUnlessPassed('test_rpm_help')
    def test_rpm_query(self):
        (status, output) = self.target.run('rpm -q rpm')
        self.assertEqual(status, 0, msg="status and output: %s and %s" % (status,output))

