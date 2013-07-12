import unittest
from oeqa.oetest import oeRuntimeTest, skipModule
from oeqa.utils.decorators import *

def setUpModule():
    if not oeRuntimeTest.hasPackage("syslog"):
        skipModule("No syslog package in image")

class SyslogTest(oeRuntimeTest):

    @skipUnlessPassed("test_ssh")
    def test_syslog_help(self):
        (status,output) = self.target.run('/sbin/syslogd --help')
        self.assertEqual(status, 1, msg="status and output: %s and %s" % (status,output))

    @skipUnlessPassed("test_syslog_help")
    def test_syslogd_running(self):
        (status,output) = self.target.run(oeRuntimeTest.pscmd + ' | grep -i [s]yslogd')
        self.assertEqual(status, 0, msg="no syslogd process, ps output: %s" % self.target.run(oeRuntimeTest.pscmd)[1])

class SyslogTestConfig(oeRuntimeTest):

    def setUp(self):
        self.target.run('echo "LOGFILE=/var/log/test" >> /etc/syslog-startup.conf')

    @skipUnlessPassed("test_syslogd_running")
    def test_syslogd_configurable(self):
        if "systemd" != oeRuntimeTest.tc.d.getVar("VIRTUAL-RUNTIME_init_manager"):
            (status,output) = self.target.run('/etc/init.d/syslog restart')
        else:
            (status,output) = self.target.run('systemctl restart syslog.service')
        self.assertEqual(status, 0, msg="Could not restart syslog service. Status and output: %s and %s" % (status,output))
        (status,output) = self.target.run('logger foobar && grep foobar /var/log/test')
        self.assertEqual(status, 0, msg="Test log string not found. Output: %s " % output)

    def tearDown(self):
        self.target.run("sed -i 's#LOGFILE=/var/log/test##' /etc/syslog-startup.conf")
