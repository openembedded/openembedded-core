#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import datetime
import os

from oeqa.runtime.case import OERuntimeTestCase

class LtpTestBase(OERuntimeTestCase):
    """
    Common setup and teardown for the LTP based test cases. Subclasses set
    ltp_log_name, the name of the log directory symlink in the test log
    directory, and ltp_result_prefix, the prefix of their extra test result
    entries.
    """
    ltp_log_name = None
    ltp_result_prefix = None

    @classmethod
    def setUpClass(cls):
        cls.ltp_startup()

    @classmethod
    def tearDownClass(cls):
        cls.ltp_finishup()

    @classmethod
    def ltp_startup(cls):
        cls.sections = {}
        cls.failmsg = ""
        test_log_dir = os.path.join(cls.td.get('WORKDIR', ''), 'testimage')
        timestamp = datetime.datetime.now().strftime('%Y%m%d%H%M%S')

        cls.ltptest_log_dir_link = os.path.join(test_log_dir, cls.ltp_log_name)
        cls.ltptest_log_dir = '%s.%s' % (cls.ltptest_log_dir_link, timestamp)
        os.makedirs(cls.ltptest_log_dir)

        cls.tc.target.run("mkdir -p /opt/ltp/results")

        if not hasattr(cls.tc, "extraresults"):
            cls.tc.extraresults = {}
        cls.extras = cls.tc.extraresults
        cls.extras['%s.rawlogs' % cls.ltp_result_prefix] = {'log': ""}

    @classmethod
    def ltp_finishup(cls):
        cls.extras['%s.sections' % cls.ltp_result_prefix] = cls.sections

        # update symlink to the log directory
        if os.path.exists(cls.ltptest_log_dir_link):
            os.remove(cls.ltptest_log_dir_link)
        os.symlink(os.path.basename(cls.ltptest_log_dir), cls.ltptest_log_dir_link)

        if cls.failmsg:
            # fail() is an instance method so can't be used from a classmethod
            raise cls.failureException(cls.failmsg)
