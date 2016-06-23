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
from . import BuildPerfTest, perf_test_case


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
