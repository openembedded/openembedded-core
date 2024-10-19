#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from subprocess import Popen, PIPE
import time
import os
from time import sleep

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.core.decorator.oetimeout import OETimeout
from oeqa.core.exception import OEQATimeoutError

class PingTest(OERuntimeTestCase):

    @OETimeout(30)
    def test_ping(self):
        output = ''
        count = 0
        self.assertNotEqual(len(self.target.ip), 0, msg="No target IP address set")

        # If the target IP is localhost (because user-space networking is being used),
        # then there's no point in pinging it.
        if self.target.ip.startswith("127.0.0.") or self.target.ip in ("localhost", "::1"):
            print("runtime/ping: localhost detected, not pinging")
            return

        try:
            while count < 5:
                cmd = 'ping -c 1 %s' % self.target.ip
                proc = Popen(cmd, shell=True, stdout=PIPE)
                output += proc.communicate()[0].decode('utf-8')
                if proc.poll() == 0:
                    count += 1
                else:
                    count = 0
                    sleep(1)
        except OEQATimeoutError:
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

            self.fail("Ping timeout error for address %s, count %s, output: %s" % (self.target.ip, count, output))
        msg = ('Expected 5 consecutive, got %d.\n'
               'ping output is:\n%s' % (count,output))
        self.assertEqual(count, 5, msg = msg)
