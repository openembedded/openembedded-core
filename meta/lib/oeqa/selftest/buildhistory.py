import os
import re
import datetime

from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import bitbake, get_bb_var
from oeqa.utils.decorators import testcase


class BuildhistoryBase(oeSelfTest):

    def config_buildhistory(self, tmp_bh_location=False):
        if (not 'buildhistory' in get_bb_var('USER_CLASSES')) and (not 'buildhistory' in get_bb_var('INHERIT')):
            add_buildhistory_config = 'INHERIT += "buildhistory"\nBUILDHISTORY_COMMIT = "1"'
            self.append_config(add_buildhistory_config)

        if tmp_bh_location:
            # Using a temporary buildhistory location for testing
            tmp_bh_dir = os.path.join(self.builddir, "tmp_buildhistory_%s" % datetime.datetime.now().strftime('%Y%m%d%H%M%S'))
            buildhistory_dir_config = "BUILDHISTORY_DIR = \"%s\"" % tmp_bh_dir
            self.append_config(buildhistory_dir_config)
            self.track_for_cleanup(tmp_bh_dir)

    def run_buildhistory_operation(self, target, global_config='', target_config='', change_bh_location=False, expect_error=False, error_regex=''):
        if change_bh_location:
            tmp_bh_location = True
        else:
            tmp_bh_location = False
        self.config_buildhistory(tmp_bh_location)

        self.append_config(global_config)
        self.append_recipeinc(target, target_config)
        bitbake("-cclean %s" % target)
        result = bitbake(target, ignore_status=True)
        self.remove_config(global_config)
        self.remove_recipeinc(target, target_config)

        if expect_error:
            self.assertEqual(result.status, 1, msg="Error expected for global config '%s' and target config '%s'" % (global_config, target_config))
            search_for_error = re.search(error_regex, result.output)
            self.assertTrue(search_for_error, msg="Could not find desired error in output: %s" % error_regex)
        else:
            self.assertEqual(result.status, 0, msg="Command 'bitbake %s' has failed unexpectedly: %s" % (target, result.output))

    @testcase(1386)
    def test_buildhistory_does_not_change_signatures(self):
        """
        Summary:     Ensure that buildhistory does not change signatures
        Expected:    Only 'do_rootfs' and 'do_build' tasks are rerun
        Product:     oe-core
        Author:      Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """

        tmpdir1_name = 'tmpsig1'
        tmpdir2_name = 'tmpsig2'
        builddir = os.environ.get('BUILDDIR')
        tmpdir1 = os.path.join(builddir, tmpdir1_name)
        tmpdir2 = os.path.join(builddir, tmpdir2_name)

        self.track_for_cleanup(tmpdir1)
        self.track_for_cleanup(tmpdir2)

        features = 'TMPDIR = "%s"\n' % tmpdir1
        self.write_config(features)
        bitbake('core-image-sato -S none')

        features = 'TMPDIR = "%s"\n' % tmpdir2
        features += 'INHERIT += "buildhistory"\n'
        self.write_config(features)
        bitbake('core-image-sato -S none')

        def get_files(d):
            f = []
            for root, dirs, files in os.walk(d):
                for name in files:
                    f.append(os.path.join(root, name))
            return f

        files1 = get_files(tmpdir1 + '/stamps')
        files2 = get_files(tmpdir2 + '/stamps')
        files2 = [x.replace(tmpdir2_name, tmpdir1_name) for x in files2]

        f1 = set(files1)
        f2 = set(files2)
        sigdiff = f1 - f2

        self.assertEqual(len(sigdiff), 2, 'Expected 2 signature differences. Out: %s' % list(sigdiff))

        unexpected_diff = []

        # No new signatures should appear apart from do_rootfs and do_build
        found_do_rootfs_flag = False
        found_do_build_flag = False

        for sig in sigdiff:
            if 'do_rootfs' in sig:
                found_do_rootfs_flag = True
            elif 'do_build' in sig:
                found_do_build_flag = True
            else:
                unexpected_diff.append(sig)

        self.assertTrue(found_do_rootfs_flag, 'Task do_rootfs did not rerun.')
        self.assertTrue(found_do_build_flag, 'Task do_build did not rerun')
        self.assertFalse(unexpected_diff, 'Found unexpected signature differences. Out: %s' % unexpected_diff)
