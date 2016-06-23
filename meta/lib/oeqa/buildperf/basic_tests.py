# Copyright (c) 2016, Intel Corporation.
#
# This program is free software; you can redistribute it and/or modify it
# under the terms and conditions of the GNU General Public License,
# version 2, as published by the Free Software Foundation.
#
# This program is distributed in the hope it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
# more details.
#
"""Basic set of build performance tests"""
import os
import shutil

from . import BuildPerfTest, perf_test_case
from oeqa.utils.commands import get_bb_vars


@perf_test_case
class Test1P1(BuildPerfTest):
    name = "test1"
    build_target = 'core-image-sato'
    description = "Measure wall clock of bitbake {} and size of tmp dir".format(build_target)

    def _run(self):
        self.log_cmd_output("bitbake {} -c fetchall".format(self.build_target))
        self.rm_tmp()
        self.rm_sstate()
        self.rm_cache()
        self.sync()
        self.measure_cmd_resources(['bitbake', self.build_target], 'build',
                                   'bitbake ' + self.build_target)
        self.measure_disk_usage(self.bb_vars['TMPDIR'], 'tmpdir', 'tmpdir')
        self.save_buildstats()


@perf_test_case
class Test1P2(BuildPerfTest):
    name = "test12"
    build_target = 'virtual/kernel'
    description = "Measure bitbake {}".format(build_target)

    def _run(self):
        self.log_cmd_output("bitbake {} -c cleansstate".format(
            self.build_target))
        self.sync()
        self.measure_cmd_resources(['bitbake', self.build_target], 'build',
                                   'bitbake ' + self.build_target)


@perf_test_case
class Test1P3(BuildPerfTest):
    name = "test13"
    build_target = 'core-image-sato'
    description = "Build {} with rm_work enabled".format(build_target)

    def _run(self):
        postfile = os.path.join(self.out_dir, 'postfile.conf')
        with open(postfile, 'w') as fobj:
            fobj.write('INHERIT += "rm_work"\n')
        try:
            self.rm_tmp()
            self.rm_sstate()
            self.rm_cache()
            self.sync()
            cmd = ['bitbake', '-R', postfile, self.build_target]
            self.measure_cmd_resources(cmd, 'build',
                                       'bitbake' + self.build_target)
            self.measure_disk_usage(self.bb_vars['TMPDIR'], 'tmpdir', 'tmpdir')
        finally:
            os.unlink(postfile)
        self.save_buildstats()


@perf_test_case
class Test2(BuildPerfTest):
    name = "test2"
    build_target = 'core-image-sato'
    description = "Measure bitbake {} -c rootfs with sstate".format(build_target)

    def _run(self):
        self.rm_tmp()
        self.rm_cache()
        self.sync()
        cmd = ['bitbake', self.build_target, '-c', 'rootfs']
        self.measure_cmd_resources(cmd, 'do_rootfs', 'bitbake do_rootfs')


@perf_test_case
class Test3(BuildPerfTest):
    name = "test3"
    description = "Parsing time metrics (bitbake -p)"

    def _run(self):
        # Drop all caches and parse
        self.rm_cache()
        self.force_rm(os.path.join(self.bb_vars['TMPDIR'], 'cache'))
        self.measure_cmd_resources(['bitbake', '-p'], 'parse_1',
                                   'bitbake -p (no caches)')
        # Drop tmp/cache
        self.force_rm(os.path.join(self.bb_vars['TMPDIR'], 'cache'))
        self.measure_cmd_resources(['bitbake', '-p'], 'parse_2',
                                   'bitbake -p (no tmp/cache)')
        # Parse with fully cached data
        self.measure_cmd_resources(['bitbake', '-p'], 'parse_3',
                                   'bitbake -p (cached)')


@perf_test_case
class Test4(BuildPerfTest):
    name = "test4"
    build_target = 'core-image-sato'
    description = "eSDK metrics"

    def _run(self):
        self.log_cmd_output("bitbake {} -c do_populate_sdk_ext".format(
            self.build_target))
        self.bb_vars = get_bb_vars(None, self.build_target)
        tmp_dir = self.bb_vars['TMPDIR']
        installer = os.path.join(
            self.bb_vars['SDK_DEPLOY'],
            self.bb_vars['TOOLCHAINEXT_OUTPUTNAME'] + '.sh')
        # Measure installer size
        self.measure_disk_usage(installer, 'installer_bin', 'eSDK installer')
        # Measure deployment time and deployed size
        deploy_dir = os.path.join(tmp_dir, 'esdk-deploy')
        if os.path.exists(deploy_dir):
            shutil.rmtree(deploy_dir)
        self.sync()
        self.measure_cmd_resources([installer, '-y', '-d', deploy_dir],
                                   'deploy', 'eSDK deploy')
        self.measure_disk_usage(deploy_dir, 'deploy_dir', 'deploy dir')
