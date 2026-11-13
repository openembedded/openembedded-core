# LTP runtime
#
# Copyright (c) 2019 MontaVista Software, LLC
#
# SPDX-License-Identifier: GPL-2.0-only
#

import os
import time
import pprint

from oeqa.runtime.utils.ltp import LtpTestBase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.runtime.decorator.package import OEHasPackage
from oeqa.utils.logparser import LtpParser

class LtpTest(LtpTestBase):
    ltp_log_name = 'ltp_log'
    ltp_result_prefix = 'ltpresult'

    ltp_groups = ["math", "syscalls", "dio", "mm", "sched", "nptl", "containers", "controllers", "fcntl-locktests", "commands", "net.ipv6_lib", "input","fs_perms_simple", "cve", "crypto", "ima", "net.nfs", "net_stress.ipsec_icmp", "net.ipv6", "numa", "uevent", "ltp-aiodio.part1", "ltp-aiodio.part2", "ltp-aiodio.part3", "ltp-aiodio.part4"]

    ltp_fs = ["fs", "fs_bind"]
    # skip kernel cpuhotplug
    ltp_kernel = ["power_management_tests", "hyperthreading ", "kernel_misc", "hugetlb"]
    ltp_groups += ltp_fs

    def runltp(self, ltp_group):
            # LTP appends to log files, so ensure we start with a clean log
            self.target.deleteFiles("/opt/ltp/results/", "%s.json" % ltp_group)

            cmd = 'kirk --run-suite %s --json-report /opt/ltp/results/%s.json -n -d /opt/ltp --exec-timeout 20m' % (ltp_group, ltp_group)

            starttime = time.time()
            (status, output) = self.target.run(cmd, timeout=1200)
            endtime = time.time()

            # status of 1 is 'just' tests failing. 255 likely was a command output timeout 
            if status and status != 1:
                msg = 'Command %s returned exit code %s' % (cmd, status)
                self.target.logger.warning(msg)

            # Write the console log to disk for convenience
            with open(os.path.join(self.ltptest_log_dir, "%s-raw.log" % ltp_group), 'w') as f:
                f.write(output)

            # Also put the console log into the test result JSON
            self.extras['ltpresult.rawlogs']['log'] = self.extras['ltpresult.rawlogs']['log'] + output

            # Copy the machine-readable test results locally so we can parse it
            dst = os.path.join(self.ltptest_log_dir, "%s.json" % ltp_group)
            remote_src = "/opt/ltp/results/%s.json" % ltp_group
            (status, output) = self.target.copyFrom(remote_src, dst, True)
            if status:
                msg = 'File could not be copied. Output: %s' % output
                self.target.logger.warning(msg)

            parser = LtpParser()
            results, sections  = parser.parse(dst)

            sections['duration'] = int(endtime-starttime)
            self.sections[ltp_group] =  sections

            failed_tests = {}
            for test in results:
                result = results[test]
                testname = ("ltpresult." + ltp_group + "." + test)
                self.extras[testname] = {'status': result}
                if result == 'FAILED':
                    failed_tests[ltp_group] = test 

            if failed_tests:
                self.failmsg = self.failmsg + "Failed ptests:\n%s" % pprint.pformat(failed_tests)

    # LTP runtime tests
    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(["ltp", "python3-kirk"])
    def test_ltp_help(self):
        (status, output) = self.target.run('kirk --help')
        msg = 'Failed to get ltp help. Output: %s' % output
        self.assertEqual(status, 0, msg=msg)

    @OETestDepends(['ltp.LtpTest.test_ltp_help'])
    def test_ltp_groups(self):
        for ltp_group in self.ltp_groups:
            try:
                self.runltp(ltp_group)
            except Exception as e:
                self.extras['ltpresult.%s' % ltp_group] = {'status': 'FAILED'}
                self.failmsg = self.failmsg + "Suite %s crashed: %s\n" % (ltp_group, e)
                self.target.logger.warning("Suite %s crashed, continuing: %s" % (ltp_group, e))

    @OETestDepends(['ltp.LtpTest.test_ltp_groups'])
    def test_ltp_runltp_cve(self):
        self.runltp("cve")
