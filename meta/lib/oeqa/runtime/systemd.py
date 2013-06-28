import unittest
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

def setUpModule():
    if not oeRuntimeTest.hasFeature("systemd"):
            skipModule("target doesn't have systemd in DISTRO_FEATURES")
    if "systemd" != oeRuntimeTest.tc.d.getVar("VIRTUAL-RUNTIME_init_manager", True):
            skipModule("systemd is not the init manager for this image")


class SystemdBasicTest(oeRuntimeTest):

    @skipUnlessPassed('test_ssh')
    def test_systemd_version(self):
        (status, output) = self.target.run('systemctl --version')
        self.assertEqual(status, 0, msg="status and output: %s and %s" % (status,output))

class SystemdTests(oeRuntimeTest):

    @skipUnlessPassed('test_systemd_version')
    def test_systemd_failed(self):
        (status, output) = self.target.run('systemctl --failed | grep "0 loaded units listed"')
        self.assertEqual(status, 0, msg="Failed systemd services: %s" % self.target.run('systemctl --failed')[1])
