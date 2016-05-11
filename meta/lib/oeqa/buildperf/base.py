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
"""Build performance test base classes and functionality"""
import logging
import os
import shutil
import time
from datetime import datetime

from oeqa.utils.commands import runCmd, get_bb_vars

# Get logger for this module
log = logging.getLogger('build-perf')


class KernelDropCaches(object):
    """Container of the functions for dropping kernel caches"""
    sudo_passwd = None

    @classmethod
    def check(cls):
        """Check permssions for dropping kernel caches"""
        from getpass import getpass
        from locale import getdefaultlocale
        cmd = ['sudo', '-k', '-n', 'tee', '/proc/sys/vm/drop_caches']
        ret = runCmd(cmd, ignore_status=True, data=b'0')
        if ret.output.startswith('sudo:'):
            pass_str = getpass(
                "\nThe script requires sudo access to drop caches between "
                "builds (echo 3 > /proc/sys/vm/drop_caches).\n"
                "Please enter your sudo password: ")
            cls.sudo_passwd = bytes(pass_str, getdefaultlocale()[1])

    @classmethod
    def drop(cls):
        """Drop kernel caches"""
        cmd = ['sudo', '-k']
        if cls.sudo_passwd:
            cmd.append('-S')
            input_data = cls.sudo_passwd + b'\n'
        else:
            cmd.append('-n')
            input_data = b''
        cmd += ['tee', '/proc/sys/vm/drop_caches']
        input_data += b'3'
        runCmd(cmd, data=input_data)


class BuildPerfTest(object):
    """Base class for build performance tests"""
    name = None
    description = None

    def __init__(self, out_dir):
        self.out_dir = out_dir
        self.results = {'name':self.name,
                        'description': self.description,
                        'status': 'NOTRUN',
                        'start_time': None,
                        'elapsed_time': None,
                        'measurements': []}
        if not os.path.exists(self.out_dir):
            os.makedirs(self.out_dir)
        if not self.name:
            self.name = self.__class__.__name__
        self.bb_vars = get_bb_vars()

    def run(self):
        """Run test"""
        self.results['status'] = 'FAILED'
        self.results['start_time'] = datetime.now()
        self._run()
        self.results['elapsed_time'] = (datetime.now() -
                                        self.results['start_time'])
        # Test is regarded as completed if it doesn't raise an exception
        self.results['status'] = 'COMPLETED'

    def _run(self):
        """Actual test payload"""
        raise NotImplementedError

    @staticmethod
    def force_rm(path):
        """Equivalent of 'rm -rf'"""
        if os.path.isfile(path) or os.path.islink(path):
            os.unlink(path)
        elif os.path.isdir(path):
            shutil.rmtree(path)

    def rm_tmp(self):
        """Cleanup temporary/intermediate files and directories"""
        log.debug("Removing temporary and cache files")
        for name in ['bitbake.lock', 'conf/sanity_info',
                     self.bb_vars['TMPDIR']]:
            self.force_rm(name)

    def rm_sstate(self):
        """Remove sstate directory"""
        log.debug("Removing sstate-cache")
        self.force_rm(self.bb_vars['SSTATE_DIR'])

    def rm_cache(self):
        """Drop bitbake caches"""
        self.force_rm(self.bb_vars['PERSISTENT_DIR'])

    @staticmethod
    def sync():
        """Sync and drop kernel caches"""
        log.debug("Syncing and dropping kernel caches""")
        KernelDropCaches.drop()
        os.sync()
        # Wait a bit for all the dirty blocks to be written onto disk
        time.sleep(3)
