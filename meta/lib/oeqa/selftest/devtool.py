import unittest
import os
import logging
import re
import shutil
import tempfile
import glob

import oeqa.utils.ftools as ftools
from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake, get_bb_var
from oeqa.utils.decorators import testcase

class DevtoolTests(oeSelfTest):

    def test_create_workspace(self):
        # Check preconditions
        workspacedir = os.path.join(self.builddir, 'workspace')
        self.assertTrue(not os.path.exists(workspacedir), 'This test cannot be run with a workspace directory under the build directory')
        result = runCmd('bitbake-layers show-layers')
        self.assertTrue('/workspace' not in result.output, 'This test cannot be run with a workspace layer in bblayers.conf')
        # Try creating a workspace layer with a specific path
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        result = runCmd('devtool create-workspace %s' % tempdir)
        self.assertTrue(os.path.isfile(os.path.join(tempdir, 'conf', 'layer.conf')))
        result = runCmd('bitbake-layers show-layers')
        self.assertIn(tempdir, result.output)
        # Try creating a workspace layer with the default path
        self.track_for_cleanup(workspacedir)
        self.add_command_to_tearDown('bitbake-layers remove-layer */workspace')
        result = runCmd('devtool create-workspace')
        self.assertTrue(os.path.isfile(os.path.join(workspacedir, 'conf', 'layer.conf')))
        result = runCmd('bitbake-layers show-layers')
        self.assertNotIn(tempdir, result.output)
        self.assertIn(workspacedir, result.output)

    def test_recipetool_create(self):
        # Try adding a recipe
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        tempsrc = os.path.join(tempdir, 'srctree')
        os.makedirs(tempsrc)
        recipefile = os.path.join(tempdir, 'logrotate_3.8.7.bb')
        srcuri = 'https://fedorahosted.org/releases/l/o/logrotate/logrotate-3.8.7.tar.gz'
        result = runCmd('recipetool create -o %s %s -x %s' % (recipefile, srcuri, tempsrc))
        self.assertTrue(os.path.isfile(recipefile))
        checkvars = {}
        checkvars['LICENSE'] = 'GPLv2'
        checkvars['LIC_FILES_CHKSUM'] = 'file://COPYING;md5=18810669f13b87348459e611d31ab760'
        checkvars['SRC_URI'] = 'https://fedorahosted.org/releases/l/o/logrotate/logrotate-${PV}.tar.gz'
        checkvars['SRC_URI[md5sum]'] = '99e08503ef24c3e2e3ff74cc5f3be213'
        checkvars['SRC_URI[sha256sum]'] = 'f6ba691f40e30e640efa2752c1f9499a3f9738257660994de70a45fe00d12b64'
        with open(recipefile, 'r') as f:
            for line in f:
                if '=' in line:
                    splitline = line.split('=', 1)
                    var = splitline[0].rstrip()
                    value = splitline[1].strip().strip('"')
                    if var in checkvars:
                        needvalue = checkvars.pop(var)
                        self.assertEqual(value, needvalue)
                if line.startswith('inherit '):
                    inherits = line.split()[1:]

        self.assertEqual(checkvars, {}, 'Some variables not found')

    def test_recipetool_create_git(self):
        # Ensure we have the right data in shlibs/pkgdata
        bitbake('libpng pango libx11 libxext')
        # Try adding a recipe
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        tempsrc = os.path.join(tempdir, 'srctree')
        os.makedirs(tempsrc)
        recipefile = os.path.join(tempdir, 'libmatchbox.bb')
        srcuri = 'git://git.yoctoproject.org/libmatchbox'
        result = runCmd('recipetool create -o %s %s -x %s' % (recipefile, srcuri, tempsrc))
        self.assertTrue(os.path.isfile(recipefile), 'recipetool did not create recipe file; output:\n%s' % result.output)
        checkvars = {}
        checkvars['LICENSE'] = 'LGPLv2.1'
        checkvars['LIC_FILES_CHKSUM'] = 'file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34'
        checkvars['S'] = '${WORKDIR}/git'
        checkvars['PV'] = '1.0+git${SRCPV}'
        checkvars['SRC_URI'] = srcuri
        checkvars['DEPENDS'] = 'libpng pango libx11 libxext'
        inherits = []
        with open(recipefile, 'r') as f:
            for line in f:
                if '=' in line:
                    splitline = line.split('=', 1)
                    var = splitline[0].rstrip()
                    value = splitline[1].strip().strip('"')
                    if var in checkvars:
                        needvalue = checkvars.pop(var)
                        self.assertEqual(value, needvalue)
                if line.startswith('inherit '):
                    inherits = line.split()[1:]

        self.assertEqual(checkvars, {}, 'Some variables not found')

        self.assertIn('autotools', inherits, 'Missing inherit of autotools')
        self.assertIn('pkgconfig', inherits, 'Missing inherit of pkgconfig')

    def test_devtool_add(self):
        # Check preconditions
        workspacedir = os.path.join(self.builddir, 'workspace')
        self.assertTrue(not os.path.exists(workspacedir), 'This test cannot be run with a workspace directory under the build directory')
        # Fetch source
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        url = 'http://www.ivarch.com/programs/sources/pv-1.5.3.tar.bz2'
        result = runCmd('wget %s' % url, cwd=tempdir)
        result = runCmd('tar xfv pv-1.5.3.tar.bz2', cwd=tempdir)
        srcdir = os.path.join(tempdir, 'pv-1.5.3')
        self.assertTrue(os.path.isfile(os.path.join(srcdir, 'configure')), 'Unable to find configure script in source directory')
        # Test devtool add
        self.track_for_cleanup(workspacedir)
        self.add_command_to_tearDown('bitbake -c cleansstate pv')
        self.add_command_to_tearDown('bitbake-layers remove-layer */workspace')
        result = runCmd('devtool add pv %s' % srcdir)
        self.assertTrue(os.path.exists(os.path.join(workspacedir, 'conf', 'layer.conf')), 'Workspace directory not created')
        # Test devtool status
        result = runCmd('devtool status')
        self.assertIn('pv', result.output)
        self.assertIn(srcdir, result.output)
        # Clean up anything in the workdir/sysroot/sstate cache (have to do this *after* devtool add since the recipe only exists then)
        bitbake('pv -c cleansstate')
        # Test devtool build
        result = runCmd('devtool build pv')
        installdir = get_bb_var('D', 'pv')
        self.assertTrue(installdir, 'Could not query installdir variable')
        bindir = get_bb_var('bindir', 'pv')
        self.assertTrue(bindir, 'Could not query bindir variable')
        if bindir[0] == '/':
            bindir = bindir[1:]
        self.assertTrue(os.path.isfile(os.path.join(installdir, bindir, 'pv')), 'pv binary not found in D')

    def test_devtool_add_library(self):
        # Check preconditions
        workspacedir = os.path.join(self.builddir, 'workspace')
        self.assertTrue(not os.path.exists(workspacedir), 'This test cannot be run with a workspace directory under the build directory')
        # We don't have the ability to pick up this dependency automatically yet...
        bitbake('libusb1')
        # Fetch source
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        url = 'http://www.intra2net.com/en/developer/libftdi/download/libftdi1-1.1.tar.bz2'
        result = runCmd('wget %s' % url, cwd=tempdir)
        result = runCmd('tar xfv libftdi1-1.1.tar.bz2', cwd=tempdir)
        srcdir = os.path.join(tempdir, 'libftdi1-1.1')
        self.assertTrue(os.path.isfile(os.path.join(srcdir, 'CMakeLists.txt')), 'Unable to find CMakeLists.txt in source directory')
        # Test devtool add
        self.track_for_cleanup(workspacedir)
        self.add_command_to_tearDown('bitbake-layers remove-layer */workspace')
        result = runCmd('devtool add libftdi %s' % srcdir)
        self.assertTrue(os.path.exists(os.path.join(workspacedir, 'conf', 'layer.conf')), 'Workspace directory not created')
        # Test devtool status
        result = runCmd('devtool status')
        self.assertIn('libftdi', result.output)
        self.assertIn(srcdir, result.output)
        # Clean up anything in the workdir/sysroot/sstate cache (have to do this *after* devtool add since the recipe only exists then)
        bitbake('libftdi -c cleansstate')
        # Test devtool build
        result = runCmd('devtool build libftdi')
        staging_libdir = get_bb_var('STAGING_LIBDIR', 'libftdi')
        self.assertTrue(staging_libdir, 'Could not query STAGING_LIBDIR variable')
        self.assertTrue(os.path.isfile(os.path.join(staging_libdir, 'libftdi1.so.2.1.0')), 'libftdi binary not found in STAGING_LIBDIR')
        # Test devtool reset
        stampprefix = get_bb_var('STAMP', 'libftdi')
        result = runCmd('devtool reset libftdi')
        result = runCmd('devtool status')
        self.assertNotIn('libftdi', result.output)
        self.assertTrue(stampprefix, 'Unable to get STAMP value for recipe libftdi')
        matches = glob.glob(stampprefix + '*')
        self.assertFalse(matches, 'Stamp files exist for recipe libftdi that should have been cleaned')
        self.assertFalse(os.path.isfile(os.path.join(staging_libdir, 'libftdi1.so.2.1.0')), 'libftdi binary still found in STAGING_LIBDIR after cleaning')

    def test_devtool_modify(self):
        # Check preconditions
        workspacedir = os.path.join(self.builddir, 'workspace')
        self.assertTrue(not os.path.exists(workspacedir), 'This test cannot be run with a workspace directory under the build directory')
        # Clean up anything in the workdir/sysroot/sstate cache
        bitbake('mdadm -c cleansstate')
        # Try modifying a recipe
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        self.track_for_cleanup(workspacedir)
        self.add_command_to_tearDown('bitbake-layers remove-layer */workspace')
        self.add_command_to_tearDown('bitbake -c clean mdadm')
        result = runCmd('devtool modify mdadm -x %s' % tempdir)
        self.assertTrue(os.path.exists(os.path.join(tempdir, 'Makefile')), 'Extracted source could not be found')
        self.assertTrue(os.path.isdir(os.path.join(tempdir, '.git')), 'git repository for external source tree not found')
        self.assertTrue(os.path.exists(os.path.join(workspacedir, 'conf', 'layer.conf')), 'Workspace directory not created')
        matches = glob.glob(os.path.join(workspacedir, 'appends', 'mdadm_*.bbappend'))
        self.assertTrue(matches, 'bbappend not created')
        # Test devtool status
        result = runCmd('devtool status')
        self.assertIn('mdadm', result.output)
        self.assertIn(tempdir, result.output)
        # Check git repo
        result = runCmd('git status --porcelain', cwd=tempdir)
        self.assertEqual(result.output.strip(), "", 'Created git repo is not clean')
        result = runCmd('git symbolic-ref HEAD', cwd=tempdir)
        self.assertEqual(result.output.strip(), "refs/heads/devtool", 'Wrong branch in git repo')
        # Try building
        bitbake('mdadm')
        # Try making (minor) modifications to the source
        result = runCmd("sed -i 's!^\.TH.*!.TH MDADM 8 \"\" v9.999-custom!' %s" % os.path.join(tempdir, 'mdadm.8.in'))
        bitbake('mdadm -c package')
        pkgd = get_bb_var('PKGD', 'mdadm')
        self.assertTrue(pkgd, 'Could not query PKGD variable')
        mandir = get_bb_var('mandir', 'mdadm')
        self.assertTrue(mandir, 'Could not query mandir variable')
        if mandir[0] == '/':
            mandir = mandir[1:]
        with open(os.path.join(pkgd, mandir, 'man8', 'mdadm.8'), 'r') as f:
            for line in f:
                if line.startswith('.TH'):
                    self.assertEqual(line.rstrip(), '.TH MDADM 8 "" v9.999-custom', 'man file not modified')
        # Test devtool reset
        stampprefix = get_bb_var('STAMP', 'mdadm')
        result = runCmd('devtool reset mdadm')
        result = runCmd('devtool status')
        self.assertNotIn('mdadm', result.output)
        self.assertTrue(stampprefix, 'Unable to get STAMP value for recipe mdadm')
        matches = glob.glob(stampprefix + '*')
        self.assertFalse(matches, 'Stamp files exist for recipe mdadm that should have been cleaned')

    def test_devtool_modify_git(self):
        # Check preconditions
        workspacedir = os.path.join(self.builddir, 'workspace')
        self.assertTrue(not os.path.exists(workspacedir), 'This test cannot be run with a workspace directory under the build directory')
        testrecipe = 'mkelfimage'
        src_uri = get_bb_var('SRC_URI', testrecipe)
        self.assertIn('git://', src_uri, 'This test expects the %s recipe to be a git recipe' % testrecipe)
        # Clean up anything in the workdir/sysroot/sstate cache
        bitbake('%s -c cleansstate' % testrecipe)
        # Try modifying a recipe
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        self.track_for_cleanup(workspacedir)
        self.add_command_to_tearDown('bitbake-layers remove-layer */workspace')
        self.add_command_to_tearDown('bitbake -c clean %s' % testrecipe)
        result = runCmd('devtool modify %s -x %s' % (testrecipe, tempdir))
        self.assertTrue(os.path.exists(os.path.join(tempdir, 'Makefile')), 'Extracted source could not be found')
        self.assertTrue(os.path.isdir(os.path.join(tempdir, '.git')), 'git repository for external source tree not found')
        self.assertTrue(os.path.exists(os.path.join(workspacedir, 'conf', 'layer.conf')), 'Workspace directory not created')
        matches = glob.glob(os.path.join(workspacedir, 'appends', 'mkelfimage_*.bbappend'))
        self.assertTrue(matches, 'bbappend not created')
        # Test devtool status
        result = runCmd('devtool status')
        self.assertIn(testrecipe, result.output)
        self.assertIn(tempdir, result.output)
        # Check git repo
        result = runCmd('git status --porcelain', cwd=tempdir)
        self.assertEqual(result.output.strip(), "", 'Created git repo is not clean')
        result = runCmd('git symbolic-ref HEAD', cwd=tempdir)
        self.assertEqual(result.output.strip(), "refs/heads/devtool", 'Wrong branch in git repo')
        # Try building
        bitbake(testrecipe)

    def test_devtool_update_recipe(self):
        # Check preconditions
        workspacedir = os.path.join(self.builddir, 'workspace')
        self.assertTrue(not os.path.exists(workspacedir), 'This test cannot be run with a workspace directory under the build directory')
        testrecipe = 'minicom'
        recipefile = get_bb_var('FILE', testrecipe)
        src_uri = get_bb_var('SRC_URI', testrecipe)
        self.assertNotIn('git://', src_uri, 'This test expects the %s recipe to NOT be a git recipe' % testrecipe)
        result = runCmd('git status . --porcelain', cwd=os.path.dirname(recipefile))
        self.assertEqual(result.output.strip(), "", '%s recipe is not clean' % testrecipe)
        # First, modify a recipe
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        self.track_for_cleanup(workspacedir)
        self.add_command_to_tearDown('bitbake-layers remove-layer */workspace')
        # (don't bother with cleaning the recipe on teardown, we won't be building it)
        result = runCmd('devtool modify %s -x %s' % (testrecipe, tempdir))
        # Check git repo
        self.assertTrue(os.path.isdir(os.path.join(tempdir, '.git')), 'git repository for external source tree not found')
        result = runCmd('git status --porcelain', cwd=tempdir)
        self.assertEqual(result.output.strip(), "", 'Created git repo is not clean')
        result = runCmd('git symbolic-ref HEAD', cwd=tempdir)
        self.assertEqual(result.output.strip(), "refs/heads/devtool", 'Wrong branch in git repo')
        # Add a couple of commits
        # FIXME: this only tests adding, need to also test update and remove
        result = runCmd('echo "Additional line" >> README', cwd=tempdir)
        result = runCmd('git commit -a -m "Change the README"', cwd=tempdir)
        result = runCmd('echo "A new file" > devtool-new-file', cwd=tempdir)
        result = runCmd('git add devtool-new-file', cwd=tempdir)
        result = runCmd('git commit -m "Add a new file"', cwd=tempdir)
        self.add_command_to_tearDown('cd %s; rm %s/*.patch; git checkout %s %s' % (os.path.dirname(recipefile), testrecipe, testrecipe, os.path.basename(recipefile)))
        result = runCmd('devtool update-recipe %s' % testrecipe)
        result = runCmd('git status . --porcelain', cwd=os.path.dirname(recipefile))
        self.assertNotEqual(result.output.strip(), "", '%s recipe should be modified' % testrecipe)
        status = result.output.splitlines()
        self.assertEqual(len(status), 3, 'Less/more files modified than expected. Entire status:\n%s' % result.output)
        for line in status:
            if line.endswith('0001-Change-the-README.patch'):
                self.assertEqual(line[:3], '?? ', 'Unexpected status in line: %s' % line)
            elif line.endswith('0002-Add-a-new-file.patch'):
                self.assertEqual(line[:3], '?? ', 'Unexpected status in line: %s' % line)
            elif re.search('%s_[^_]*.bb$' % testrecipe, line):
                self.assertEqual(line[:3], ' M ', 'Unexpected status in line: %s' % line)
            else:
                raise AssertionError('Unexpected modified file in status: %s' % line)

    def test_devtool_update_recipe_git(self):
        # Check preconditions
        workspacedir = os.path.join(self.builddir, 'workspace')
        self.assertTrue(not os.path.exists(workspacedir), 'This test cannot be run with a workspace directory under the build directory')
        testrecipe = 'mtd-utils'
        recipefile = get_bb_var('FILE', testrecipe)
        src_uri = get_bb_var('SRC_URI', testrecipe)
        self.assertIn('git://', src_uri, 'This test expects the %s recipe to be a git recipe' % testrecipe)
        result = runCmd('git status . --porcelain', cwd=os.path.dirname(recipefile))
        self.assertEqual(result.output.strip(), "", '%s recipe is not clean' % testrecipe)
        # First, modify a recipe
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        self.track_for_cleanup(tempdir)
        self.track_for_cleanup(workspacedir)
        self.add_command_to_tearDown('bitbake-layers remove-layer */workspace')
        # (don't bother with cleaning the recipe on teardown, we won't be building it)
        result = runCmd('devtool modify %s -x %s' % (testrecipe, tempdir))
        # Check git repo
        self.assertTrue(os.path.isdir(os.path.join(tempdir, '.git')), 'git repository for external source tree not found')
        result = runCmd('git status --porcelain', cwd=tempdir)
        self.assertEqual(result.output.strip(), "", 'Created git repo is not clean')
        result = runCmd('git symbolic-ref HEAD', cwd=tempdir)
        self.assertEqual(result.output.strip(), "refs/heads/devtool", 'Wrong branch in git repo')
        # Add a couple of commits
        # FIXME: this only tests adding, need to also test update and remove
        result = runCmd('echo "# Additional line" >> Makefile', cwd=tempdir)
        result = runCmd('git commit -a -m "Change the Makefile"', cwd=tempdir)
        result = runCmd('echo "A new file" > devtool-new-file', cwd=tempdir)
        result = runCmd('git add devtool-new-file', cwd=tempdir)
        result = runCmd('git commit -m "Add a new file"', cwd=tempdir)
        self.add_command_to_tearDown('cd %s; git checkout %s %s' % (os.path.dirname(recipefile), testrecipe, os.path.basename(recipefile)))
        result = runCmd('devtool update-recipe %s' % testrecipe)
        result = runCmd('git status . --porcelain', cwd=os.path.dirname(recipefile))
        self.assertNotEqual(result.output.strip(), "", '%s recipe should be modified' % testrecipe)
        status = result.output.splitlines()
        self.assertEqual(len(status), 3, 'Less/more files modified than expected. Entire status:\n%s' % result.output)
        for line in status:
            if line.endswith('add-exclusion-to-mkfs-jffs2-git-2.patch'):
                self.assertEqual(line[:3], ' D ', 'Unexpected status in line: %s' % line)
            elif line.endswith('fix-armv7-neon-alignment.patch'):
                self.assertEqual(line[:3], ' D ', 'Unexpected status in line: %s' % line)
            elif re.search('%s_[^_]*.bb$' % testrecipe, line):
                self.assertEqual(line[:3], ' M ', 'Unexpected status in line: %s' % line)
            else:
                raise AssertionError('Unexpected modified file in status: %s' % line)
        result = runCmd('git diff %s' % os.path.basename(recipefile), cwd=os.path.dirname(recipefile))
        addlines = ['SRCREV = ".*"', 'SRC_URI = "git://git.infradead.org/mtd-utils.git"']
        removelines = ['SRCREV = ".*"', 'SRC_URI = "git://git.infradead.org/mtd-utils.git \\\\', 'file://add-exclusion-to-mkfs-jffs2-git-2.patch \\\\', 'file://fix-armv7-neon-alignment.patch \\\\', '"']
        for line in result.output.splitlines():
            if line.startswith('+++') or line.startswith('---'):
                continue
            elif line.startswith('+'):
                matched = False
                for item in addlines:
                    if re.match(item, line[1:].strip()):
                        matched = True
                        break
                self.assertTrue(matched, 'Unexpected diff add line: %s' % line)
            elif line.startswith('-'):
                matched = False
                for item in removelines:
                    if re.match(item, line[1:].strip()):
                        matched = True
                        break
                self.assertTrue(matched, 'Unexpected diff remove line: %s' % line)

    def test_devtool_extract(self):
        # Check preconditions
        workspacedir = os.path.join(self.builddir, 'workspace')
        self.assertTrue(not os.path.exists(workspacedir), 'This test cannot be run with a workspace directory under the build directory')
        tempdir = tempfile.mkdtemp(prefix='devtoolqa')
        # Try devtool extract
        self.track_for_cleanup(tempdir)
        self.track_for_cleanup(workspacedir)
        self.add_command_to_tearDown('bitbake-layers remove-layer */workspace')
        result = runCmd('devtool extract remake %s' % tempdir)
        self.assertTrue(os.path.exists(os.path.join(tempdir, 'Makefile.am')), 'Extracted source could not be found')
        self.assertTrue(os.path.isdir(os.path.join(tempdir, '.git')), 'git repository for external source tree not found')
