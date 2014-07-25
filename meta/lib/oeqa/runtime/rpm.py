import unittest
import os
import fnmatch
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

def setUpModule():
    if not oeRuntimeTest.hasFeature("package-management"):
            skipModule("rpm module skipped: target doesn't have package-management in IMAGE_FEATURES")
    if "package_rpm" != oeRuntimeTest.tc.d.getVar("PACKAGE_CLASSES", True).split()[0]:
            skipModule("rpm module skipped: target doesn't have rpm as primary package manager")


class RpmBasicTest(oeRuntimeTest):

    @skipUnlessPassed('test_ssh')
    def test_rpm_help(self):
        (status, output) = self.target.run('rpm --help')
        self.assertEqual(status, 0, msg="status and output: %s and %s" % (status,output))

    @testcase(191)
    @skipUnlessPassed('test_rpm_help')
    def test_rpm_query(self):
        (status, output) = self.target.run('rpm -q rpm')
        self.assertEqual(status, 0, msg="status and output: %s and %s" % (status,output))

class RpmInstallRemoveTest(oeRuntimeTest):

    @classmethod
    def setUpClass(self):
        pkgarch = oeRuntimeTest.tc.d.getVar('TUNE_PKGARCH', True).replace("-", "_")
        rpmdir = os.path.join(oeRuntimeTest.tc.d.getVar('DEPLOY_DIR', True), "rpm", pkgarch)
        # pick rpm-doc as a test file to get installed, because it's small and it will always be built for standard targets
        for f in fnmatch.filter(os.listdir(rpmdir), "rpm-doc-*.%s.rpm" % pkgarch):
            testrpmfile = f
        oeRuntimeTest.tc.target.copy_to(os.path.join(rpmdir,testrpmfile), "/tmp/rpm-doc.rpm")

    @testcase(192)
    @skipUnlessPassed('test_rpm_help')
    def test_rpm_install(self):
        (status, output) = self.target.run('rpm -ivh /tmp/rpm-doc.rpm')
        self.assertEqual(status, 0, msg="Failed to install rpm-doc package: %s" % output)

    @testcase(194)
    @skipUnlessPassed('test_rpm_install')
    def test_rpm_remove(self):
        (status,output) = self.target.run('rpm -e rpm-doc')
        self.assertEqual(status, 0, msg="Failed to remove rpm-doc package: %s" % output)

    @classmethod
    def tearDownClass(self):
        oeRuntimeTest.tc.target.run('rm -f /tmp/rpm-doc.rpm')

