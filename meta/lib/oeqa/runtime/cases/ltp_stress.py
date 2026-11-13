# LTP Stress runtime
#
# Copyright (c) 2019 MontaVista Software, LLC
#
# SPDX-License-Identifier: MIT
#

import os
import time
import pprint

from oeqa.runtime.utils.ltp import LtpTestBase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.runtime.decorator.package import OEHasPackage
from oeqa.core.decorator.data import skipIfQemu
from oeqa.utils.logparser import LtpParser

class LtpStressTest(LtpTestBase):
    ltp_log_name = 'ltpstress_log'
    ltp_result_prefix = 'ltpstressresult'

    def runltp(self, stress_group):
            cmd = 'kirk --run-suite %s --json-report /opt/ltp/results/%s.json -n -d /opt/ltp' % (stress_group, stress_group)
            starttime = time.time()
            (status, output) = self.target.run(cmd)
            endtime = time.time()
            with open(os.path.join(self.ltptest_log_dir, "%s" % stress_group), 'w') as f:
                f.write(output)

            self.extras['ltpstressresult.rawlogs']['log'] = self.extras['ltpstressresult.rawlogs']['log'] + output

            # Copy kirk JSON report from target
            dst = os.path.join(self.ltptest_log_dir, "%s.json" % stress_group)
            remote_src = "/opt/ltp/results/%s.json" % stress_group
            (status, output) = self.target.copyFrom(remote_src, dst, True)
            if status:
                msg = 'File could not be copied. Output: %s' % output
                self.target.logger.warning(msg)

            parser = LtpParser()
            results, sections  = parser.parse(dst)

            runtime = int(endtime-starttime)
            sections['duration'] = runtime
            self.sections[stress_group] =  sections
 
            failed_tests = {}
            for test in results:
                result = results[test]
                testname = ("ltpstressresult." + stress_group + "." + test)
                self.extras[testname] = {'status': result}
                if result == 'FAILED':
                    failed_tests[stress_group] = test 

            if failed_tests:
                self.failmsg = self.failmsg + "Failed ptests:\n%s" % pprint.pformat(failed_tests)

    # LTP stress runtime tests
    #
    @skipIfQemu()
    @OETestDepends(['ssh.SSHTest.test_ssh'])
    @OEHasPackage(["ltp"])
    def test_ltp_stress(self):
        self.tc.target.run("sed -i -r 's/^fork12.*//' /opt/ltp/runtest/crashme")
        self.runltp('crashme')
