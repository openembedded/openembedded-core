#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import os
import time
import signal

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.runtime.decorator.package import OEHasPackage

class SSHTest(OERuntimeTestCase):

    @OETestDepends(['ping.PingTest.test_ping'])
    @OEHasPackage(['dropbear', 'openssh-sshd'])
    def test_ssh(self):
        for i in range(5):
          status, output = self.target.run("uname -a", timeout=30)
          if status == 0:
              break
          elif status == 255 or status == -signal.SIGTERM:
              # ssh returns 255 only if a ssh error occurs.  This could
              # be an issue with "Connection refused" because the port
              # isn't open yet, and this could check explicitly for that
              # here.  However, let's keep it simple and just retry for
              # all errors a limited amount of times with a sleep to
              # give it time for the port to open.
              # We sometimes see -15 (SIGTERM) on slow emulation machines too, likely
              # from boot/init not being 100% complete, retry for these too.
              time.sleep(5)
              continue
          else:
              status, output = self.target.runner.run_serial("ifconfig")
              print("ifconfig on target: %s %s" % (output, status))
              status, output = self.target.runner.run_serial("ping -c 1 %s" % self.target.server_ip)
              print("ping on target: %s %s %s" % (self.target.server_ip, output, status))
              status, output = self.target.runner.run_serial("ping -c 1 %s" % self.target.ip)
              print("ping on target: %s %s %s" % (self.target.ip, output, status))
              os.system("/usr/bin/netstat -tunape")
              os.system("/usr/bin/netstat -ei")
              os.system("ps -awx")
              print("PID: %s %s" % (str(os.getpid()), time.time()))
              self.fail("uname failed with \"%s\" (exit code %s)" % (output, status))
        if status != 0:
            self.fail("ssh failed with \"%s\" (exit code %s)" % (output, status))
