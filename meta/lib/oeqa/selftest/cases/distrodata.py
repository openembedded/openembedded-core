from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import runCmd, bitbake, get_bb_var, get_bb_vars
from oeqa.utils.decorators import testcase
from oeqa.utils.ftools import write_file

class Distrodata(OESelftestTestCase):

    @classmethod
    def setUpClass(cls):
        super(Distrodata, cls).setUpClass()
        cls.exceptions_path = os.path.join(cls.testlayer_path, 'files', 'distrodata', "checkpkg_exceptions")

    def test_checkpkg(self):
        """
        Summary:     Test that upstream version checks do not regress
        Expected:    Upstream version checks should succeed except for the recipes listed in the exception list.
        Product:     oe-core
        Author:      Alexander Kanavin <alexander.kanavin@intel.com>
        """
        feature = 'INHERIT += "distrodata"\n'
        feature += 'LICENSE_FLAGS_WHITELIST += " commercial"\n'

        self.write_config(feature)
        bitbake('-c checkpkg world')
        checkpkg_result = open(os.path.join(get_bb_var("LOG_DIR"), "checkpkg.csv")).readlines()[1:]
        exceptions = [exc.strip() for exc in open(self.exceptions_path).readlines()]
        failed_upstream_checks = [pkg_data[0] for pkg_data in [pkg_line.split('\t') for pkg_line in checkpkg_result] if pkg_data[11] == '']
        regressed_failures = set(failed_upstream_checks) - set(exceptions)
        regressed_successes = set(exceptions) - set(failed_upstream_checks)
        msg = ""
        if len(regressed_failures) > 0:
            msg = msg + """
The following packages failed upstream version checks. Please fix them using UPSTREAM_CHECK_URI/UPSTREAM_CHECK_REGEX
(when using tarballs) or UPSTREAM_CHECK_GITTAGREGEX (when using git). If an upstream version check cannot be performed
(for example, if upstream does not use git tags), you can add the package to list of exceptions in
meta-selftest/files/distrodata/checkpkg_exceptions.
""" + "\n".join(regressed_failures)
        if len(regressed_successes) > 0:
            msg = msg + """
The following packages have been checked successfully for upstream versions (or they no longer exist in oe-core),
but are in the exceptions list in meta-selftest/files/distrodata/checkpkg_exceptions. Please remove them from that list.
""" + "\n".join(regressed_successes)
        self.assertTrue(len(regressed_failures) == 0 and len(regressed_successes) == 0, msg)
