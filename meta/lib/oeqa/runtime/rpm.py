import unittest
import os
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *
import oe.packagedata

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

    @skipUnlessPassed('test_rpm_help')
    def test_rpm_query(self):
        (status, output) = self.target.run('rpm -q rpm')
        self.assertEqual(status, 0, msg="status and output: %s and %s" % (status,output))

class RpmInstallRemoveTest(oeRuntimeTest):

    @classmethod
    def setUpClass(self):
        deploydir = os.path.join(oeRuntimeTest.tc.d.getVar('DEPLOY_DIR', True), "rpm", oeRuntimeTest.tc.d.getVar('TUNE_PKGARCH', True))
        pkgdata = oe.packagedata.read_subpkgdata("rpm-doc", oeRuntimeTest.tc.d)
        # pick rpm-doc as a test file to get installed, because it's small and it will always be built for standard targets
        testrpmfile = "rpm-doc-%s-%s.%s.rpm" % (pkgdata["PKGV"], pkgdata["PKGR"], oeRuntimeTest.tc.d.getVar('TUNE_PKGARCH', True))
        oeRuntimeTest.tc.target.copy_to(os.path.join(deploydir,testrpmfile), "/tmp/rpm-doc.rpm")

    @skipUnlessPassed('test_rpm_help')
    def test_rpm_install(self):
        (status, output) = self.target.run('rpm -ivh /tmp/rpm-doc.rpm')
        self.assertEqual(status, 0, msg="Failed to install rpm-doc package: %s" % output)

    @skipUnlessPassed('test_rpm_install')
    def test_rpm_remove(self):
        (status,output) = self.target.run('rpm -e rpm-doc')
        self.assertEqual(status, 0, msg="Failed to remove rpm-doc package: %s" % output)

    @classmethod
    def tearDownClass(self):
        oeRuntimeTest.tc.target.run('rm -f /tmp/rpm-doc.rpm')

