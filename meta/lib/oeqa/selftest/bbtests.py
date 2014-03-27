import unittest
import os
import logging
import re
import shutil

import oeqa.utils.ftools as ftools
from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake, get_bb_var

class BitbakeTests(oeSelfTest):

    def test_run_bitbake_from_dir_1(self):
        os.chdir(os.path.join(self.builddir, 'conf'))
        bitbake('-e')

    def test_run_bitbake_from_dir_2(self):
        my_env = os.environ.copy()
        my_env['BBPATH'] = my_env['BUILDDIR']
        os.chdir(os.path.dirname(os.environ['BUILDDIR']))
        bitbake('-e', env=my_env)

    def test_event_handler(self):
        self.write_config("INHERIT += \"test_events\"")
        result = bitbake('m4-native')
        find_build_started = re.search("NOTE: Test for bb\.event\.BuildStarted(\n.*)*NOTE: Preparing runqueue", result.output)
        find_build_completed = re.search("Tasks Summary:.*(\n.*)*NOTE: Test for bb\.event\.BuildCompleted", result.output)
        self.assertTrue(find_build_started, msg = "Match failed in:\n%s"  % result.output)
        self.assertTrue(find_build_completed, msg = "Match failed in:\n%s" % result.output)
        self.assertFalse('Test for bb.event.InvalidEvent' in result.output)

    def test_local_sstate(self):
        bitbake('m4-native -ccleansstate')
        bitbake('m4-native')
        bitbake('m4-native -cclean')
        result = bitbake('m4-native')
        find_setscene = re.search("m4-native.*do_.*_setscene", result.output)
        self.assertTrue(find_setscene)

    def test_bitbake_invalid_recipe(self):
        result = bitbake('-b asdf', ignore_status=True)
        self.assertTrue("ERROR: Unable to find any recipe file matching 'asdf'" in result.output)

    def test_bitbake_invalid_target(self):
        result = bitbake('asdf', ignore_status=True)
        self.assertTrue("ERROR: Nothing PROVIDES 'asdf'" in result.output)

    def test_warnings_errors(self):
        result = bitbake('-b asdf', ignore_status=True)
        find_warnings = re.search("Summary: There w.{2,3}? [1-9][0-9]* WARNING messages* shown", result.output)
        find_errors = re.search("Summary: There w.{2,3}? [1-9][0-9]* ERROR messages* shown", result.output)
        self.assertTrue(find_warnings, msg="Did not find the mumber of warnings at the end of the build:\n" + result.output)
        self.assertTrue(find_errors, msg="Did not find the mumber of errors at the end of the build:\n" + result.output)

    def test_invalid_patch(self):
        self.write_recipeinc('man', 'SRC_URI += "file://man-1.5h1-make.patch"')
        result = bitbake('man -c patch', ignore_status=True)
        self.delete_recipeinc('man')
        bitbake('-cclean man')
        self.assertTrue("ERROR: Function failed: patch_do_patch" in result.output)

    def test_force_task(self):
        bitbake('m4-native')
        result = bitbake('-C compile m4-native')
        look_for_tasks = ['do_compile', 'do_install', 'do_populate_sysroot']
        for task in look_for_tasks:
            find_task = re.search("m4-native.*%s" % task, result.output)
            self.assertTrue(find_task)

    def test_bitbake_g(self):
        result = bitbake('-g core-image-full-cmdline')
        self.assertTrue('NOTE: PN build list saved to \'pn-buildlist\'' in result.output)
        self.assertTrue('openssh' in ftools.read_file(os.path.join(self.builddir, 'pn-buildlist')))
        for f in ['pn-buildlist', 'pn-depends.dot', 'package-depends.dot', 'task-depends.dot']:
            os.remove(f)

    def test_image_manifest(self):
        bitbake('core-image-minimal')
        deploydir = get_bb_var("DEPLOY_DIR_IMAGE", target="core-image-minimal")
        imagename = get_bb_var("IMAGE_LINK_NAME", target="core-image-minimal")
        manifest = os.path.join(deploydir, imagename + ".manifest")
        self.assertTrue(os.path.islink(manifest), msg="No manifest file created for image")

    def test_invalid_recipe_src_uri(self):
        data = 'SRC_URI = "file://invalid"'
        self.write_recipeinc('man', data)
        bitbake('-ccleanall man')
        result = bitbake('-c fetch man', ignore_status=True)
        bitbake('-ccleanall man')
        self.delete_recipeinc('man')
        self.assertEqual(result.status, 1, msg='Command succeded when it should have failed')
        self.assertTrue('ERROR: Fetcher failure: Unable to find file file://invalid anywhere. The paths that were searched were:' in result.output)
        self.assertTrue('ERROR: Function failed: Fetcher failure for URL: \'file://invalid\'. Unable to fetch URL from any source.' in result.output)

    def test_rename_downloaded_file(self):
        data = 'SRC_URI_append = ";downloadfilename=test-aspell.tar.gz"'
        self.write_recipeinc('aspell', data)
        bitbake('-ccleanall aspell')
        result = bitbake('-c fetch aspell', ignore_status=True)
        self.delete_recipeinc('aspell')
        self.assertEqual(result.status, 0)
        self.assertTrue(os.path.isfile(os.path.join(get_bb_var("DL_DIR"), 'test-aspell.tar.gz')))
        self.assertTrue(os.path.isfile(os.path.join(get_bb_var("DL_DIR"), 'test-aspell.tar.gz.done')))
        bitbake('-ccleanall aspell')
