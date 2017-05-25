# Copyright (C) 2013-2017 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import sys
import os
import shutil
import glob
import errno
from random import choice
from unittest.util import safe_repr

import oeqa.utils.ftools as ftools
from oeqa.utils.commands import runCmd, bitbake, get_bb_var, get_test_layer
from oeqa.core.case import OETestCase

class OESelftestTestCase(OETestCase):
    def __init__(self, methodName="runTest"):
        self.builddir = os.environ.get("BUILDDIR")
        self.localconf_path = os.path.join(self.builddir, "conf/local.conf")
        self.localconf_backup = os.path.join(self.builddir, "conf/local.bk")
        self.testinc_path = os.path.join(self.builddir, "conf/selftest.inc")
        self.local_bblayers_path = os.path.join(self.builddir, "conf/bblayers.conf")
        self.local_bblayers_backup = os.path.join(self.builddir,
                                                  "conf/bblayers.bk")
        self.testinc_bblayers_path = os.path.join(self.builddir, "conf/bblayers.inc")
        self.machineinc_path = os.path.join(self.builddir, "conf/machine.inc")
        self.testlayer_path = get_test_layer()
        self._extra_tear_down_commands = []
        self._track_for_cleanup = [
            self.testinc_path, self.testinc_bblayers_path,
            self.machineinc_path, self.localconf_backup,
            self.local_bblayers_backup]

        super(OESelftestTestCase, self).__init__(methodName)

    def setUp(self):
        super(OESelftestTestCase, self).setUp()
        os.chdir(self.builddir)
        # Check if local.conf or bblayers.conf files backup exists
        # from a previous failed test and restore them
        if os.path.isfile(self.localconf_backup) or os.path.isfile(
                self.local_bblayers_backup):
            self.logger.debug("\
Found a local.conf and/or bblayers.conf backup from a previously aborted test.\
Restoring these files now, but tests should be re-executed from a clean environment\
to ensure accurate results.")
            try:
                shutil.copyfile(self.localconf_backup, self.localconf_path)
            except OSError as e:
                if e.errno != errno.ENOENT:
                    raise
            try:
                shutil.copyfile(self.local_bblayers_backup,
                                self.local_bblayers_path)
            except OSError as e:
                if e.errno != errno.ENOENT:
                    raise
        else:
            # backup local.conf and bblayers.conf
            shutil.copyfile(self.localconf_path, self.localconf_backup)
            shutil.copyfile(self.local_bblayers_path, self.local_bblayers_backup)
            self.logger.debug("Creating local.conf and bblayers.conf backups.")
        # we don't know what the previous test left around in config or inc files
        # if it failed so we need a fresh start
        try:
            os.remove(self.testinc_path)
        except OSError as e:
            if e.errno != errno.ENOENT:
                raise
        for root, _, files in os.walk(self.testlayer_path):
            for f in files:
                if f == 'test_recipe.inc':
                    os.remove(os.path.join(root, f))

        for incl_file in [self.testinc_bblayers_path, self.machineinc_path]:
            try:
                os.remove(incl_file)
            except OSError as e:
                if e.errno != errno.ENOENT:
                    raise

        # Get CUSTOMMACHINE from env (set by --machine argument to oe-selftest)
        custommachine = os.getenv('CUSTOMMACHINE')
        if custommachine:
            if custommachine == 'random':
                machine = get_random_machine()
            else:
                machine = custommachine
            machine_conf = 'MACHINE ??= "%s"\n' % machine
            self.set_machine_config(machine_conf)
            print('MACHINE: %s' % machine)

        # tests might need their own setup
        # but if they overwrite this one they have to call
        # super each time, so let's give them an alternative
        self.setUpLocal()

    def setUpLocal(self):
        pass

    def tearDown(self):
        if self._extra_tear_down_commands:
            failed_extra_commands = []
            for command in self._extra_tear_down_commands:
                result = runCmd(command, ignore_status=True)
                if not result.status ==  0:
                    failed_extra_commands.append(command)
            if failed_extra_commands:
                self.logger.warning("tearDown commands have failed: %s" % ', '.join(map(str, failed_extra_commands)))
                self.logger.debug("Trying to move on.")
            self._extra_tear_down_commands = []

        if self._track_for_cleanup:
            for path in self._track_for_cleanup:
                if os.path.isdir(path):
                    shutil.rmtree(path)
                if os.path.isfile(path):
                    os.remove(path)
            self._track_for_cleanup = []

        self.tearDownLocal()
        super(OESelftestTestCase, self).tearDown()

    def tearDownLocal(self):
        pass

    def add_command_to_tearDown(self, command):
        """Add test specific commands to the tearDown method"""
        self.logger.debug("Adding command '%s' to tearDown for this test." % command)
        self._extra_tear_down_commands.append(command)

    def track_for_cleanup(self, path):
        """Add test specific files or directories to be removed in the tearDown method"""
        self.logger.debug("Adding path '%s' to be cleaned up when test is over" % path)
        self._track_for_cleanup.append(path)

    def write_config(self, data):
        """Write to <builddir>/conf/selftest.inc"""

        self.logger.debug("Writing to: %s\n%s\n" % (self.testinc_path, data))
        ftools.write_file(self.testinc_path, data)

        custommachine = os.getenv('CUSTOMMACHINE')
        if custommachine and 'MACHINE' in data:
            machine = get_bb_var('MACHINE')
            self.logger.warning('MACHINE overridden: %s' % machine)

    def append_config(self, data):
        """Append to <builddir>/conf/selftest.inc"""
        self.logger.debug("Appending to: %s\n%s\n" % (self.testinc_path, data))
        ftools.append_file(self.testinc_path, data)

        custommachine = os.getenv('CUSTOMMACHINE')
        if custommachine and 'MACHINE' in data:
            machine = get_bb_var('MACHINE')
            self.logger.warning('MACHINE overridden: %s' % machine)

    def remove_config(self, data):
        """Remove data from <builddir>/conf/selftest.inc"""
        self.logger.debug("Removing from: %s\n%s\n" % (self.testinc_path, data))
        ftools.remove_from_file(self.testinc_path, data)

    def write_recipeinc(self, recipe, data):
        """Write to meta-sefltest/recipes-test/<recipe>/test_recipe.inc"""
        inc_file = os.path.join(self.testlayer_path, 'recipes-test', recipe, 'test_recipe.inc')
        self.logger.debug("Writing to: %s\n%s\n" % (inc_file, data))
        ftools.write_file(inc_file, data)

    def append_recipeinc(self, recipe, data):
        """Append data to meta-sefltest/recipes-test/<recipe>/test_recipe.inc"""
        inc_file = os.path.join(self.testlayer_path, 'recipes-test', recipe, 'test_recipe.inc')
        self.logger.debug("Appending to: %s\n%s\n" % (inc_file, data))
        ftools.append_file(inc_file, data)

    def remove_recipeinc(self, recipe, data):
        """Remove data from meta-sefltest/recipes-test/<recipe>/test_recipe.inc"""
        inc_file = os.path.join(self.testlayer_path, 'recipes-test', recipe, 'test_recipe.inc')
        self.logger.debug("Removing from: %s\n%s\n" % (inc_file, data))
        ftools.remove_from_file(inc_file, data)

    def delete_recipeinc(self, recipe):
        """Delete meta-sefltest/recipes-test/<recipe>/test_recipe.inc file"""
        inc_file = os.path.join(self.testlayer_path, 'recipes-test', recipe, 'test_recipe.inc')
        self.logger.debug("Deleting file: %s" % inc_file)
        try:
            os.remove(inc_file)
        except OSError as e:
            if e.errno != errno.ENOENT:
                raise
    def write_bblayers_config(self, data):
        """Write to <builddir>/conf/bblayers.inc"""
        self.logger.debug("Writing to: %s\n%s\n" % (self.testinc_bblayers_path, data))
        ftools.write_file(self.testinc_bblayers_path, data)

    def append_bblayers_config(self, data):
        """Append to <builddir>/conf/bblayers.inc"""
        self.logger.debug("Appending to: %s\n%s\n" % (self.testinc_bblayers_path, data))
        ftools.append_file(self.testinc_bblayers_path, data)

    def remove_bblayers_config(self, data):
        """Remove data from <builddir>/conf/bblayers.inc"""
        self.logger.debug("Removing from: %s\n%s\n" % (self.testinc_bblayers_path, data))
        ftools.remove_from_file(self.testinc_bblayers_path, data)

    def set_machine_config(self, data):
        """Write to <builddir>/conf/machine.inc"""
        self.logger.debug("Writing to: %s\n%s\n" % (self.machineinc_path, data))
        ftools.write_file(self.machineinc_path, data)

    # check does path exist    
    def assertExists(self, expr, msg=None):
        if not os.path.exists(expr):
            msg = self._formatMessage(msg, "%s does not exist" % safe_repr(expr))
            raise self.failureException(msg)
    
    # check does path not exist 
    def assertNotExists(self, expr, msg=None):
        if os.path.exists(expr):
            msg = self._formatMessage(msg, "%s exists when it should not" % safe_repr(expr))
            raise self.failureException(msg)

def get_available_machines():
    # Get a list of all available machines
    bbpath = get_bb_var('BBPATH').split(':')
    machines = []

    for path in bbpath:
        found_machines = glob.glob(os.path.join(path, 'conf', 'machine', '*.conf'))
        if found_machines:
            for i in found_machines:
            # eg: '/home/<user>/poky/meta-intel/conf/machine/intel-core2-32.conf'
                machines.append(os.path.splitext(os.path.basename(i))[0])

    return machines


def get_random_machine():
    # Get a random machine
    return choice(get_available_machines())
