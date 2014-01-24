import datetime
import unittest
import os
import re
import shutil

import oeqa.utils.ftools as ftools
from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake, get_bb_var, get_test_layer
from oeqa.selftest.sstate import SStateBase


class RebuildFromSState(SStateBase):

    @classmethod
    def setUpClass(self):
        self.builddir = os.path.join(os.environ.get('BUILDDIR'))

    def get_dep_targets(self, primary_targets):
        found_targets = []
        bitbake("-g " + ' '.join(map(str, primary_targets)))
        with open(os.path.join(self.builddir, 'pn-buildlist'), 'r') as pnfile:
            found_targets = pnfile.read().splitlines()
        return found_targets

    def configure_builddir(self, builddir):
        if os.path.exists(builddir):
            raise AssertionError("Cannot create build directory at %s: Path allready exists!" % builddir)
        try:
            os.mkdir(builddir)
        except:
            raise AssertionError("Cannot create %s . Make sure %s exists!" % (dst, os.path.dirname(dst)))
        os.mkdir(os.path.join(builddir, 'conf'))
        shutil.copyfile(os.path.join(os.environ.get('BUILDDIR'), 'conf/local.conf'), os.path.join(builddir, 'conf/local.conf'))
        shutil.copyfile(os.path.join(os.environ.get('BUILDDIR'), 'conf/bblayers.conf'), os.path.join(builddir, 'conf/bblayers.conf'))

    def hardlink_tree(self, src, dst):
        if os.path.exists(dst):
            raise AssertionError("Cannot create directory at %s: Path allready exists!" % dst)
        try:
            os.mkdir(dst)
        except:
            raise AssertionError("Cannot create %s . Make sure %s exists!" % (dst, os.path.dirname(dst)))
        for root, dirs, files in os.walk(src):
            if root == src:
                continue
            os.mkdir(os.path.join(dst, root.split(src)[1][1:]))
            for sstate_file in files:
                os.link(os.path.join(root, sstate_file), os.path.join(dst, root.split(src)[1][1:], sstate_file))

    def run_test_sstate_rebuild(self, primary_targets, relocate=False, rebuild_dependencies=False):
        buildA = os.path.join(self.builddir, 'buildA')
        if relocate:
            buildB = os.path.join(self.builddir, 'buildB')
        else:
            buildB = buildA
        self.track_for_cleanup(buildA)
        self.track_for_cleanup(buildB)
        self.track_for_cleanup(os.path.join(self.builddir, 'sstate-cache-buildA'))

        if rebuild_dependencies:
            rebuild_targets = self.get_dep_targets(primary_targets)
        else:
            rebuild_targets = primary_targets

        self.configure_builddir(buildA)
        runCmd((". %s/oe-init-build-env %s && " % (get_bb_var('COREBASE'), buildA)) + 'bitbake  ' + ' '.join(map(str, primary_targets)), shell=True, executable='/bin/bash')
        self.hardlink_tree(os.path.join(buildA, 'sstate-cache'), os.path.join(self.builddir, 'sstate-cache-buildA'))
        shutil.rmtree(buildA)

        failed_rebuild = []
        failed_cleansstate = []
        for target in rebuild_targets:
            self.configure_builddir(buildB)
            self.hardlink_tree(os.path.join(self.builddir, 'sstate-cache-buildA'), os.path.join(buildB, 'sstate-cache'))

            result_cleansstate = runCmd((". %s/oe-init-build-env %s && " % (get_bb_var('COREBASE'), buildB)) + 'bitbake -ccleansstate ' + target, ignore_status=True, shell=True, executable='/bin/bash')
            if not result_cleansstate.status == 0:
                failed_cleansstate.append(target)
                shutil.rmtree(buildB)
                continue

            result_build = runCmd((". %s/oe-init-build-env %s && " % (get_bb_var('COREBASE'), buildB)) + 'bitbake ' + target, ignore_status=True, shell=True, executable='/bin/bash')
            if not result_build.status == 0:
                failed_rebuild.append(target)

            shutil.rmtree(buildB)

        self.assertFalse(failed_rebuild, msg="The following recipes have failed to rebuild: %s" % ' '.join(map(str, failed_rebuild)))
        self.assertFalse(failed_cleansstate, msg="The following recipes have failed cleansstate(all others have passed both cleansstate and rebuild from sstate tests): %s" % ' '.join(map(str, failed_cleansstate)))

    def test_sstate_relocation(self):
        self.run_test_sstate_rebuild(['core-image-sato-sdk'], relocate=True, rebuild_dependencies=True)

    def test_sstate_rebuild(self):
        self.run_test_sstate_rebuild(['core-image-sato-sdk'], relocate=False, rebuild_dependencies=True)
