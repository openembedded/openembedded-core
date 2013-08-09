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

    @skipUnlessPassed('test_systemd_version')
    def test_systemd_service(self):
        (status, output) = self.target.run('systemctl list-unit-files | grep "systemd-hostnamed.service"')
        self.assertEqual(status, 0, msg="systemd-hostnamed.service service is not available.")

    @skipUnlessPassed('test_systemd_service')
    def test_systemd_stop(self):
        self.target.run('systemctl stop systemd-hostnamed.service')
        (status, output) = self.target.run('systemctl show systemd-hostnamed.service | grep "ActiveState" | grep "=inactive"')
        self.assertEqual(status, 0, msg="systemd-hostnamed.service service could not be stopped.Status and output: %s and %s" % (status, output))

    @skipUnlessPassed('test_systemd_stop')
    @skipUnlessPassed('test_systemd_version')
    def test_systemd_start(self):
        self.target.run('systemctl start systemd-hostnamed.service')
        (status, output) = self.target.run('systemctl show systemd-hostnamed.service | grep "ActiveState" | grep "=active"')
        self.assertEqual(status, 0, msg="systemd-hostnamed.service service could not be started. Status and output: %s and %s" % (status, output))

    @skipUnlessPassed('test_systemd_version')
    def test_systemd_enable(self):
        self.target.run('systemctl enable machineid.service')
        (status, output) = self.target.run('systemctl is-enabled machineid.service')
        self.assertEqual(output, 'enabled', msg="machineid.service service could not be enabled. Status and output: %s and %s" % (status, output))

    @skipUnlessPassed('test_systemd_enable')
    def test_systemd_disable(self):
        self.target.run('systemctl disable machineid.service')
        (status, output) = self.target.run('systemctl is-enabled machineid.service')
        self.assertEqual(output, 'disabled', msg="machineid.service service could not be disabled. Status and output: %s and %s" % (status, output))

    @skipUnlessPassed('test_systemd_version')
    def test_systemd_list(self):
        (status, output) = self.target.run('systemctl list-unit-files')
        self.assertEqual(status, 0, msg="systemctl list-unit-files command failed. Status:  %s" % status)
