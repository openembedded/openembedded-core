# Copyright (c) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)


# DESCRIPTION
# Base class inherited by test classes in meta/lib/selftest

import unittest
import os
import sys
import logging
import errno

import oeqa.utils.ftools as ftools


class oeSelfTest(unittest.TestCase):

    log = logging.getLogger("selftest.base")
    longMessage = True

    def __init__(self, methodName="runTest"):
        self.builddir = os.environ.get("BUILDDIR")
        self.localconf_path = os.path.join(self.builddir, "conf/local.conf")
        self.testinc_path = os.path.join(self.builddir, "conf/selftest.inc")
        self.testlayer_path = oeSelfTest.testlayer_path
        super(oeSelfTest, self).__init__(methodName)

    def setUp(self):
        os.chdir(self.builddir)
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
        # tests might need their own setup
        # but if they overwrite this one they have to call
        # super each time, so let's give them an alternative
        self.setUpLocal()

    def setUpLocal(self):
        pass

    def tearDown(self):
        self.tearDownLocal()

    def tearDownLocal(self):
        pass

    # write to <builddir>/conf/selftest.inc
    def write_config(self, data):
        self.log.debug("Writing to: %s\n%s\n" % (self.testinc_path, data))
        ftools.write_file(self.testinc_path, data)

    # append to <builddir>/conf/selftest.inc
    def append_config(self, data):
        self.log.debug("Appending to: %s\n%s\n" % (self.testinc_path, data))
        ftools.append_file(self.testinc_path, data)

    # remove data from <builddir>/conf/selftest.inc
    def remove_config(self, data):
        self.log.debug("Removing from: %s\n\%s\n" % (self.testinc_path, data))
        ftools.remove_from_file(self.testinc_path, data)

    # write to meta-sefltest/recipes-test/<recipe>/test_recipe.inc
    def write_recipeinc(self, recipe, data):
        inc_file = os.path.join(self.testlayer_path, 'recipes-test', recipe, 'test_recipe.inc')
        self.log.debug("Writing to: %s\n%s\n" % (inc_file, data))
        ftools.write_file(inc_file, data)

    # append data to meta-sefltest/recipes-test/<recipe>/test_recipe.inc
    def append_recipeinc(self, recipe, data):
        inc_file = os.path.join(self.testlayer_path, 'recipes-test', recipe, 'test_recipe.inc')
        self.log.debug("Appending to: %s\n%s\n" % (inc_file, data))
        ftools.append_file(inc_file, data)

    # remove data from meta-sefltest/recipes-test/<recipe>/test_recipe.inc
    def remove_recipeinc(self, recipe, data):
        inc_file = os.path.join(self.testlayer_path, 'recipes-test', recipe, 'test_recipe.inc')
        self.log.debug("Removing from: %s\n%s\n" % (inc_file, data))
        ftools.remove_from_file(inc_file, data)

    # delete meta-sefltest/recipes-test/<recipe>/test_recipe.inc file
    def delete_recipeinc(self, recipe):
        inc_file = os.path.join(self.testlayer_path, 'recipes-test', recipe, 'test_recipe.inc')
        self.log.debug("Deleting file: %s" % inc_file)
        try:
            os.remove(self.testinc_path)
        except OSError as e:
            if e.errno != errno.ENOENT:
                raise
