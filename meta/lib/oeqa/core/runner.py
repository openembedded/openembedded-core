# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os
import time
import unittest
import logging
import re

xmlEnabled = False
try:
    import xmlrunner
    from xmlrunner.result import _XMLTestResult as _TestResult
    from xmlrunner.runner import XMLTestRunner as _TestRunner
    xmlEnabled = True
except ImportError:
    # use the base runner instead
    from unittest import TextTestResult as _TestResult
    from unittest import TextTestRunner as _TestRunner

class OEStreamLogger(object):
    def __init__(self, logger):
        self.logger = logger
        self.buffer = ""

    def write(self, msg):
        if len(msg) > 1 and msg[0] != '\n':
            self.buffer += msg
        else:
            self.logger.log(logging.INFO, self.buffer.rstrip("\n"))
            self.buffer = ""

    def flush(self):
        for handler in self.logger.handlers:
            handler.flush()

class OETestResult(_TestResult):
    def __init__(self, tc, *args, **kwargs):
        super(OETestResult, self).__init__(*args, **kwargs)

        self.tc = tc
        self._tc_map_results()

    def _tc_map_results(self):
        self.tc._results['failures'] = self.failures
        self.tc._results['errors'] = self.errors
        self.tc._results['skipped'] = self.skipped
        self.tc._results['expectedFailures'] = self.expectedFailures

    def logSummary(self, component, context_msg=''):
        elapsed_time = self.tc._run_end_time - self.tc._run_start_time
        self.tc.logger.info("SUMMARY:")
        self.tc.logger.info("%s (%s) - Ran %d test%s in %.3fs" % (component,
            context_msg, self.testsRun, self.testsRun != 1 and "s" or "",
            elapsed_time))

        if self.wasSuccessful():
            msg = "%s - OK - All required tests passed" % component
        else:
            msg = "%s - FAIL - Required tests failed" % component
        skipped = len(self.tc._results['skipped'])
        if skipped: 
            msg += " (skipped=%d)" % skipped
        self.tc.logger.info(msg)

    def _getDetailsNotPassed(self, case, type, desc):
        found = False

        for (scase, msg) in self.tc._results[type]:
            # XXX: When XML reporting is enabled scase is
            # xmlrunner.result._TestInfo instance instead of
            # string.
            if xmlEnabled:
                if case.id() == scase.test_id:
                    found = True
                    break
                scase_str = scase.test_id
            else:
                if case == scase:
                    found = True
                    break
                scase_str = str(scase)

            # When fails at module or class level the class name is passed as string
            # so figure out to see if match
            m = re.search("^setUpModule \((?P<module_name>.*)\)$", scase_str)
            if m:
                if case.__class__.__module__ == m.group('module_name'):
                    found = True
                    break

            m = re.search("^setUpClass \((?P<class_name>.*)\)$", scase_str)
            if m:
                class_name = "%s.%s" % (case.__class__.__module__,
                        case.__class__.__name__)

                if class_name == m.group('class_name'):
                    found = True
                    break

        if found:
            return (found, msg)

        return (found, None)

    def logDetails(self):
        self.tc.logger.info("RESULTS:")
        for case_name in self.tc._registry['cases']:
            case = self.tc._registry['cases'][case_name]

            result_types = ['failures', 'errors', 'skipped', 'expectedFailures']
            result_desc = ['FAILED', 'ERROR', 'SKIPPED', 'EXPECTEDFAIL']

            fail = False
            desc = None
            for idx, name in enumerate(result_types):
                (fail, msg) = self._getDetailsNotPassed(case, result_types[idx],
                        result_desc[idx])
                if fail:
                    desc = result_desc[idx]
                    break

            oeid = -1
            for d in case.decorators:
                if hasattr(d, 'oeid'):
                    oeid = d.oeid

            if fail:
                self.tc.logger.info("RESULTS - %s - Testcase %s: %s" % (case.id(),
                    oeid, desc))
                if msg:
                    self.tc.logger.info(msg)
            else:
                self.tc.logger.info("RESULTS - %s - Testcase %s: %s" % (case.id(),
                    oeid, 'PASSED'))

class OETestRunner(_TestRunner):
    streamLoggerClass = OEStreamLogger

    def __init__(self, tc, *args, **kwargs):
        if xmlEnabled:
            if not kwargs.get('output'):
                kwargs['output'] = os.path.join(os.getcwd(),
                        'TestResults_%s_%s' % (time.strftime("%Y%m%d%H%M%S"), os.getpid()))

        kwargs['stream'] = self.streamLoggerClass(tc.logger)
        super(OETestRunner, self).__init__(*args, **kwargs)
        self.tc = tc
        self.resultclass = OETestResult

    # XXX: The unittest-xml-reporting package defines _make_result method instead
    # of _makeResult standard on unittest.
    if xmlEnabled:
        def _make_result(self):
            """
            Creates a TestResult object which will be used to store
            information about the executed tests.
            """
            # override in subclasses if necessary.
            return self.resultclass(self.tc,
                self.stream, self.descriptions, self.verbosity, self.elapsed_times
            )
    else:
        def _makeResult(self):
            return self.resultclass(self.tc, self.stream, self.descriptions,
                    self.verbosity)
