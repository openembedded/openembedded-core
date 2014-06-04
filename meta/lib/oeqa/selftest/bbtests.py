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

    def test_environment(self):
	self.append_config("TEST_ENV=\"localconf\"")
	result = runCmd('bitbake -e | grep TEST_ENV=')
	self.assertTrue('localconf' in result.output)
	self.remove_config("TEST_ENV=\"localconf\"")

    def test_dry_run(self):
	result = runCmd('bitbake -n m4-native')
	self.assertEqual(0, result.status)

    def test_just_parse(self):
	result = runCmd('bitbake -p')
	self.assertEqual(0, result.status)

    def test_version(self):
	result = runCmd('bitbake -s | grep wget')
	find = re.search("wget *:([0-9a-zA-Z\.\-]+)", result.output)
	self.assertTrue(find) 

    def test_prefile(self):
	preconf = os.path.join(self.builddir, 'conf/prefile.conf')
	self.track_for_cleanup(preconf)
	ftools.write_file(preconf ,"TEST_PREFILE=\"prefile\"")
	result = runCmd('bitbake -r conf/prefile.conf -e | grep TEST_PREFILE=')
	self.assertTrue('prefile' in result.output)
	self.append_config("TEST_PREFILE=\"localconf\"")
	result = runCmd('bitbake -r conf/prefile.conf -e | grep TEST_PREFILE=')
	self.assertTrue('localconf' in result.output)
	self.remove_config("TEST_PREFILE=\"localconf\"")

    def test_postfile(self):
	postconf = os.path.join(self.builddir, 'conf/postfile.conf')
	self.track_for_cleanup(postconf)
	ftools.write_file(postconf , "TEST_POSTFILE=\"postfile\"")
	self.append_config("TEST_POSTFILE=\"localconf\"")
	result = runCmd('bitbake -R conf/postfile.conf -e | grep TEST_POSTFILE=')
	self.assertTrue('postfile' in result.output)
	self.remove_config("TEST_POSTFILE=\"localconf\"")

    def test_checkuri(self):
	result = runCmd('bitbake -c checkuri m4')
	self.assertEqual(0, result.status)

    def test_continue(self):
	self.write_recipeinc('man',"\ndo_fail_task () {\nexit 1 \n}\n\naddtask do_fail_task before do_fetch\n" )
	runCmd('bitbake -c cleanall man xcursor-transparent-theme')
	result = runCmd('bitbake man xcursor-transparent-theme -k', ignore_status=True)
	errorpos = result.output.find('ERROR: Function failed: do_fail_task')
	manver = re.search("NOTE: recipe xcursor-transparent-theme-(.*?): task do_unpack: Started", result.output)
	continuepos = result.output.find('NOTE: recipe xcursor-transparent-theme-%s: task do_unpack: Started' % manver.group(1))
	self.assertLess(errorpos,continuepos)
