# LTP compliance runtime
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
from oeqa.utils.logparser import LtpComplianceParser

class LtpPosixTest(LtpTestBase):
    ltp_log_name = 'ltpcomp_log'
    ltp_result_prefix = 'ltpposixresult'

    posix_groups = ["AIO", "MEM", "MSG", "SEM", "SIG",  "THR", "TMR", "TPS"]

    def runltp(self, posix_group):
            cmd = "/opt/ltp/bin/run-posix-option-group-test.sh %s 2>@1 | tee /opt/ltp/results/%s" % (posix_group, posix_group)
            starttime = time.time()
            (status, output) = self.target.run(cmd)
            endtime = time.time()

            with open(os.path.join(self.ltptest_log_dir, "%s" % posix_group), 'w') as f:
                f.write(output)

            self.extras['ltpposixresult.rawlogs']['log'] = self.extras['ltpposixresult.rawlogs']['log'] + output

            parser = LtpComplianceParser()
            results, sections  = parser.parse(os.path.join(self.ltptest_log_dir, "%s" % posix_group))

            runtime = int(endtime-starttime)
            sections['duration'] = runtime
            self.sections[posix_group] =  sections
 
            failed_tests = {}
            for test in results:
                result = results[test]
                testname = ("ltpposixresult." + posix_group + "." + test)
                self.extras[testname] = {'status': result}
                if result == 'FAILED':
                    failed_tests[posix_group] = test 

            if failed_tests:
                self.failmsg = self.failmsg + "Failed ptests:\n%s" % pprint.pformat(failed_tests)

    # LTP Posix compliance runtime tests

    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(["ltp"])
    def test_posix_groups(self):
        for posix_group in self.posix_groups: 
            self.runltp(posix_group)
