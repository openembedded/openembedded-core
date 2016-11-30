# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import shutil
import subprocess

from oeqa.sdkext.case import OESDKExtTestCase
from oeqa.core.decorator.depends import OETestDepends
from oeqa.core.decorator.oeid import OETestID

class DevtoolTest(OESDKExtTestCase):
    @classmethod
    def setUpClass(self):
        self.myapp_src = os.path.join(self.tc.esdk_files_dir, "myapp")
        self.myapp_dst = os.path.join(self.tc.sdk_dir, "myapp")
        shutil.copytree(self.myapp_src, self.myapp_dst)

        self.myapp_cmake_src = os.path.join(self.tc.esdk_files_dir, "myapp_cmake")
        self.myapp_cmake_dst = os.path.join(self.tc.sdk_dir, "myapp_cmake")
        shutil.copytree(self.myapp_cmake_src, self.myapp_cmake_dst)

    def _test_devtool_build(self, directory):
        self._run('devtool add myapp %s' % directory)
        try:
            self._run('devtool build myapp')
        except Exception as e:
            print(e.output)
            self._run('devtool reset myapp')
            raise e
        self._run('devtool reset myapp')

    def _test_devtool_build_package(self, directory):
        self._run('devtool add myapp %s' % directory)
        try:
            self._run('devtool package myapp')
        except Exception as e:
            print(e.output)
            self._run('devtool reset myapp')
            raise e
        self._run('devtool reset myapp')

    def test_devtool_location(self):
        output = self._run('which devtool')
        self.assertEqual(output.startswith(self.tc.sdk_dir), True, \
            msg="Seems that devtool isn't the eSDK one: %s" % output)
    
    @OETestDepends(['test_devtool_location'])
    def test_devtool_add_reset(self):
        self._run('devtool add myapp %s' % self.myapp_dst)
        self._run('devtool reset myapp')
    
    @OETestID(1473)
    @OETestDepends(['test_devtool_location'])
    def test_devtool_build_make(self):
        self._test_devtool_build(self.myapp_dst)
    
    @OETestID(1474)
    @OETestDepends(['test_devtool_location'])
    def test_devtool_build_esdk_package(self):
        self._test_devtool_build_package(self.myapp_dst)

    @OETestID(1479)
    @OETestDepends(['test_devtool_location'])
    def test_devtool_build_cmake(self):
        self._test_devtool_build(self.myapp_cmake_dst)
    
    @OETestID(1482)
    @OETestDepends(['test_devtool_location'])
    def test_extend_autotools_recipe_creation(self):
        req = 'https://github.com/rdfa/librdfa'
        recipe = "bbexample"
        self._run('devtool add %s %s' % (recipe, req) )
        try:
            self._run('devtool build %s' % recipe)
        except Exception as e:
            print(e.output)
            self._run('devtool reset %s' % recipe)
            raise e
        self._run('devtool reset %s' % recipe)

    @OETestID(1484)
    @OETestDepends(['test_devtool_location'])
    def test_devtool_kernelmodule(self):
        docfile = 'https://github.com/umlaeute/v4l2loopback.git'
        recipe = 'v4l2loopback-driver'
        self._run('devtool add %s %s' % (recipe, docfile) )
        try:
            self._run('devtool build %s' % recipe)
        except Exception as e:
            print(e.output)
            self._run('devtool reset %s' % recipe)
            raise e
        self._run('devtool reset %s' % recipe)

    @OETestID(1478)
    @OETestDepends(['test_devtool_location'])
    def test_recipes_for_nodejs(self):
        package_nodejs = "npm://registry.npmjs.org;name=winston;version=2.2.0"
        self._run('devtool add %s ' % package_nodejs)
        try:
            self._run('devtool build %s ' % package_nodejs)
        except Exception as e:
            print(e.output)
            self._run('devtool reset %s' % package_nodejs)
            raise e
        self._run('devtool reset %s '% package_nodejs)

    @classmethod
    def tearDownClass(self):
        shutil.rmtree(self.myapp_dst)
        shutil.rmtree(self.myapp_cmake_dst)
