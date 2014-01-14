import datetime
import unittest
import os
import re
import shutil

import oeqa.utils.ftools as ftools
from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake, get_bb_var, get_test_layer

class SStateBase(oeSelfTest):

    def setUpLocal(self):
        self.temp_sstate_location = None
        self.sstate_path = get_bb_var('SSTATE_DIR')
        self.distro = get_bb_var('NATIVELSBSTRING')
        self.distro_specific_sstate = os.path.join(self.sstate_path, self.distro)

    # Creates a special sstate configuration with the option to add sstate mirrors
    def config_sstate(self, temp_sstate_location=False, add_local_mirrors=[]):
        self.temp_sstate_location = temp_sstate_location

        if self.temp_sstate_location:
            temp_sstate_path = os.path.join(self.builddir, "temp_sstate_%s" % datetime.datetime.now().strftime('%Y%m%d%H%M%S'))
            config_temp_sstate = "SSTATE_DIR = \"%s\"" % temp_sstate_path
            self.append_config(config_temp_sstate)
            self.track_for_cleanup(temp_sstate_path)
        self.sstate_path = get_bb_var('SSTATE_DIR')
        self.distro = get_bb_var('NATIVELSBSTRING')
        self.distro_specific_sstate = os.path.join(self.sstate_path, self.distro)

        if add_local_mirrors:
            config_set_sstate_if_not_set = 'SSTATE_MIRRORS ?= ""'
            self.append_config(config_set_sstate_if_not_set)
            for local_mirror in add_local_mirrors:
                self.assertFalse(os.path.join(local_mirror) == os.path.join(self.sstate_path), msg='Cannot add the current sstate path as a sstate mirror')
                config_sstate_mirror = "SSTATE_MIRRORS += \"file://.* file:///%s/PATH\"" % local_mirror
                self.append_config(config_sstate_mirror)

    # Returns a list containing sstate files
    def search_sstate(self, filename_regex, distro_specific=True, distro_nonspecific=True):
        result = []
        for root, dirs, files in os.walk(self.sstate_path):
            if distro_specific and re.search("%s/[a-z0-9]{2}$" % self.distro, root):
                for f in files:
                    if re.search(filename_regex, f):
                        result.append(f)
            if distro_nonspecific and re.search("%s/[a-z0-9]{2}$" % self.sstate_path, root):
                for f in files:
                    if re.search(filename_regex, f):
                        result.append(f)
        return result

class SStateTests(SStateBase):

    # Test sstate files creation and their location
    def run_test_sstate_creation(self, targets, distro_specific=True, distro_nonspecific=True, temp_sstate_location=True, should_pass=True):
        self.config_sstate(temp_sstate_location)

        if  self.temp_sstate_location:
            bitbake(['-cclean'] + targets)
        else:
            bitbake(['-ccleansstate'] + targets)

        bitbake(targets)
        file_tracker = self.search_sstate('|'.join(map(str, targets)), distro_specific, distro_nonspecific)
        if should_pass:
            self.assertTrue(file_tracker , msg="Could not find sstate files for: %s" % ', '.join(map(str, targets)))
        else:
            self.assertTrue(not file_tracker , msg="Found sstate files in the wrong place for: %s" % ', '.join(map(str, targets)))

    def test_sstate_creation_distro_specific_pass(self):
        self.run_test_sstate_creation(['binutils-cross', 'binutils-native'], distro_specific=True, distro_nonspecific=False, temp_sstate_location=True)

    def test_sstate_creation_distro_specific_fail(self):
        self.run_test_sstate_creation(['binutils-cross', 'binutils-native'], distro_specific=False, distro_nonspecific=True, temp_sstate_location=True, should_pass=False)

    def test_sstate_creation_distro_nonspecific_pass(self):
        self.run_test_sstate_creation(['eglibc-initial'], distro_specific=False, distro_nonspecific=True, temp_sstate_location=True)

    def test_sstate_creation_distro_nonspecific_fail(self):
        self.run_test_sstate_creation(['eglibc-initial'], distro_specific=True, distro_nonspecific=False, temp_sstate_location=True, should_pass=False)


    # Test the sstate files deletion part of the do_cleansstate task
    def run_test_cleansstate_task(self, targets, distro_specific=True, distro_nonspecific=True, temp_sstate_location=True):
        self.config_sstate(temp_sstate_location)

        bitbake(['-ccleansstate'] + targets)

        bitbake(targets)
        file_tracker_1 = self.search_sstate('|'.join(map(str, targets)), distro_specific, distro_nonspecific)
        self.assertTrue(file_tracker_1, msg="Could not find sstate files for: %s" % ', '.join(map(str, targets)))

        bitbake(['-ccleansstate'] + targets)
        file_tracker_2 = self.search_sstate('|'.join(map(str, targets)), distro_specific, distro_nonspecific)
        self.assertTrue(not file_tracker_2)

    def test_cleansstate_task_distro_specific_nonspecific(self):
        self.run_test_cleansstate_task(['binutils-cross', 'binutils-native', 'eglibc-initial'], distro_specific=True, distro_nonspecific=True, temp_sstate_location=True)

    def test_cleansstate_task_distro_nonspecific(self):
        self.run_test_cleansstate_task(['eglibc-initial'], distro_specific=False, distro_nonspecific=True, temp_sstate_location=True)

    def test_cleansstate_task_distro_specific(self):
        self.run_test_cleansstate_task(['binutils-cross', 'binutils-native', 'eglibc-initial'], distro_specific=True, distro_nonspecific=False, temp_sstate_location=True)


    # Test rebuilding of distro-specific sstate files
    def run_test_rebuild_distro_specific_sstate(self, targets, temp_sstate_location=True):
        self.config_sstate(temp_sstate_location)

        bitbake(['-ccleansstate'] + targets)

        bitbake(targets)
        self.assertTrue(self.search_sstate('|'.join(map(str, targets)), distro_specific=False, distro_nonspecific=True) == [], msg="Found distro non-specific sstate for: %s" % ', '.join(map(str, targets)))
        file_tracker_1 = self.search_sstate('|'.join(map(str, targets)), distro_specific=True, distro_nonspecific=False)
        self.assertTrue(len(file_tracker_1) > len(targets), msg = "Not all sstate files ware created for: %s" % ', '.join(map(str, targets)))

        self.track_for_cleanup(self.distro_specific_sstate + "_old")
        shutil.copytree(self.distro_specific_sstate, self.distro_specific_sstate + "_old")
        shutil.rmtree(self.distro_specific_sstate)

        bitbake(['-cclean'] + targets)
        bitbake(targets)
        file_tracker_2 = self.search_sstate('|'.join(map(str, targets)), distro_specific=True, distro_nonspecific=False)
        self.assertTrue(len(file_tracker_2) > len(targets), msg = "Not all sstate files ware created for: %s" % ', '.join(map(str, targets)))

        not_recreated = [x for x in file_tracker_1 if x not in file_tracker_2]
        self.assertTrue(not_recreated == [], msg="The following sstate files ware not recreated: %s" % ', '.join(map(str, not_recreated)))

        created_once = [x for x in file_tracker_2 if x not in file_tracker_1]
        self.assertTrue(created_once == [], msg="The following sstate files ware created only in the second run: %s" % ', '.join(map(str, created_once)))

    def test_rebuild_distro_specific_sstate_cross_native_targets(self):
        self.run_test_rebuild_distro_specific_sstate(['binutils-cross', 'binutils-native'], temp_sstate_location=True)

    def test_rebuild_distro_specific_sstate_cross_target(self):
        self.run_test_rebuild_distro_specific_sstate(['binutils-cross'], temp_sstate_location=True)

    def test_rebuild_distro_specific_sstate_native_target(self):
        self.run_test_rebuild_distro_specific_sstate(['binutils-native'], temp_sstate_location=True)


    # Test the sstate-cache-management script. Each element in the global_config list is used with the corresponding element in the target_config list
    def run_test_sstate_cache_management_script(self, target, global_config=[''], target_config=['']):
        self.assertTrue(global_config)
        self.assertTrue(target_config)
        self.assertTrue(len(global_config) == len(target_config), msg='Lists global_config and target_config should have the same number of elements')
        self.config_sstate(temp_sstate_location=True, add_local_mirrors=[self.sstate_path])

        # If buildhistory is enabled, we need to disable version-going-backwards QA checks for this test. It may report errors otherwise.
        if ('buildhistory' in get_bb_var('USER_CLASSES')) or ('buildhistory' in get_bb_var('INHERIT')):
            remove_errors_config = 'ERROR_QA_remove = "version-going-backwards"'
            self.append_config(remove_errors_config)

        # For not this only checks if random sstate tasks are handled correctly as a group.
        # In the future we should add control over what tasks we check for.

        expected_remaining_sstate = []
        for idx in range(len(target_config)):
            self.append_config(global_config[idx])
            self.append_recipeinc(target, target_config[idx])
            if target_config[idx] == target_config[-1]:
                target_sstate_before_build = self.search_sstate(target)
            bitbake("-cclean %s" % target)
            result = bitbake(target, ignore_status=True)
            if target_config[idx] == target_config[-1]:
                target_sstate_after_build = self.search_sstate(target)
                expected_remaining_sstate += [x for x in target_sstate_after_build if x not in target_sstate_before_build]
            self.remove_config(global_config[idx])
            self.remove_recipeinc(target, target_config[idx])
            self.assertEqual(result.status, 0)

        runCmd("sstate-cache-management.sh -y --cache-dir=%s --remove-duplicated" % self.sstate_path)
        actual_remaining_sstate = self.search_sstate(target)

        actual_not_expected = [x for x in actual_remaining_sstate if x not in expected_remaining_sstate]
        self.assertFalse(actual_not_expected, msg="Files should have been removed but ware not: %s" % ', '.join(map(str, actual_not_expected)))
        expected_not_actual = [x for x in expected_remaining_sstate if x not in actual_remaining_sstate]
        self.assertFalse(expected_not_actual, msg="Extra files ware removed: %s" ', '.join(map(str, expected_not_actual)))


    def test_sstate_cache_management_script_using_pr_1(self):
        global_config = []
        target_config = []
        global_config.append('')
        target_config.append('PR = "0"')
        self.run_test_sstate_cache_management_script('m4', global_config,  target_config)

    def test_sstate_cache_management_script_using_pr_2(self):
        global_config = []
        target_config = []
        global_config.append('')
        target_config.append('PR = "0"')
        global_config.append('')
        target_config.append('PR = "1"')
        self.run_test_sstate_cache_management_script('m4', global_config,  target_config)

    def test_sstate_cache_management_script_using_pr_3(self):
        global_config = []
        target_config = []
        global_config.append('MACHINE = "qemux86-64"')
        target_config.append('PR = "0"')
        global_config.append(global_config[0])
        target_config.append('PR = "1"')
        global_config.append('MACHINE = "qemux86"')
        target_config.append('PR = "1"')
        self.run_test_sstate_cache_management_script('m4', global_config,  target_config)

    def test_sstate_cache_management_script_using_machine(self):
        global_config = []
        target_config = []
        global_config.append('MACHINE = "qemux86-64"')
        target_config.append('')
        global_config.append('MACHINE = "qemux86"')
        target_config.append('')
        self.run_test_sstate_cache_management_script('m4', global_config,  target_config)









