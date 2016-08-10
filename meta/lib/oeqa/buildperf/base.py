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
import glob
import logging
import os
import re
import shutil
import socket
import tempfile
import time
import traceback
from datetime import datetime, timedelta

from oeqa.utils.commands import runCmd, get_bb_vars
from oeqa.utils.git import GitError, GitRepo

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


def time_cmd(cmd, **kwargs):
    """TIme a command"""
    with tempfile.NamedTemporaryFile(mode='w+') as tmpf:
        timecmd = ['/usr/bin/time', '-v', '-o', tmpf.name]
        if isinstance(cmd, str):
            timecmd = ' '.join(timecmd) + ' '
        timecmd += cmd
        # TODO: 'ignore_status' could/should be removed when globalres.log is
        # deprecated. The function would just raise an exception, instead
        ret = runCmd(timecmd, ignore_status=True, **kwargs)
        timedata = tmpf.file.read()
    return ret, timedata


class BuildPerfTestRunner(object):
    """Runner class for executing the individual tests"""
    # List of test cases to run
    test_run_queue = []

    def __init__(self, out_dir):
        self.results = {}
        self.out_dir = os.path.abspath(out_dir)
        if not os.path.exists(self.out_dir):
            os.makedirs(self.out_dir)

        # Get Git parameters
        try:
            self.repo = GitRepo('.')
        except GitError:
            self.repo = None
        self.git_rev, self.git_branch = self.get_git_revision()
        log.info("Using Git branch:revision %s:%s", self.git_branch,
                 self.git_rev)

    def get_git_revision(self):
        """Get git branch and revision under testing"""
        rev = os.getenv('OE_BUILDPERFTEST_GIT_REVISION')
        branch = os.getenv('OE_BUILDPERFTEST_GIT_BRANCH')
        if not self.repo and (not rev or not branch):
            log.info("The current working directory doesn't seem to be a Git "
                     "repository clone. You can specify branch and revision "
                     "used in test results with OE_BUILDPERFTEST_GIT_REVISION "
                     "and OE_BUILDPERFTEST_GIT_BRANCH environment variables")
        else:
            if not rev:
                rev = self.repo.run_cmd(['rev-parse', 'HEAD'])
            if not branch:
                try:
                    # Strip 11 chars, i.e. 'refs/heads' from the beginning
                    branch = self.repo.run_cmd(['symbolic-ref', 'HEAD'])[11:]
                except GitError:
                    log.debug('Currently on detached HEAD')
                    branch = None
        return str(rev), str(branch)

    def run_tests(self):
        """Method that actually runs the tests"""
        self.results['schema_version'] = 1
        self.results['git_revision'] = self.git_rev
        self.results['git_branch'] = self.git_branch
        self.results['tester_host'] = socket.gethostname()
        start_time = datetime.utcnow()
        self.results['start_time'] = start_time
        self.results['tests'] = {}

        self.archive_build_conf()
        for test_class in self.test_run_queue:
            log.info("Executing test %s: %s", test_class.name,
                     test_class.description)

            test = test_class(self.out_dir)
            try:
                test.run()
            except Exception:
                # Catch all exceptions. This way e.g buggy tests won't scrap
                # the whole test run
                sep = '-' * 5 + ' TRACEBACK ' + '-' * 60 + '\n'
                tb_msg = sep + traceback.format_exc() + sep
                log.error("Test execution failed with:\n" + tb_msg)
            self.results['tests'][test.name] = test.results

        self.results['elapsed_time'] = datetime.utcnow() - start_time
        return 0

    def archive_build_conf(self):
        """Archive build/conf to test results"""
        src_dir = os.path.join(os.environ['BUILDDIR'], 'conf')
        tgt_dir = os.path.join(self.out_dir, 'build', 'conf')
        os.makedirs(os.path.dirname(tgt_dir))
        shutil.copytree(src_dir, tgt_dir)

    def update_globalres_file(self, filename):
        """Write results to globalres csv file"""
        if self.repo:
            git_tag_rev = self.repo.run_cmd(['describe', self.git_rev])
        else:
            git_tag_rev = self.git_rev
        times = []
        sizes = []
        for test in self.results['tests'].values():
            for measurement in test['measurements']:
                res_type = measurement['type']
                values = measurement['values']
                if res_type == BuildPerfTest.SYSRES:
                    e_sec = values['elapsed_time'].total_seconds()
                    times.append('{:d}:{:02d}:{:.2f}'.format(
                        int(e_sec / 3600),
                        int((e_sec % 3600) / 60),
                        e_sec % 60))
                elif res_type == BuildPerfTest.DISKUSAGE:
                    sizes.append(str(values['size']))
                else:
                    log.warning("Unable to handle '%s' values in "
                                "globalres.log", res_type)

        log.debug("Writing globalres log to %s", filename)
        with open(filename, 'a') as fobj:
            fobj.write('{},{}:{},{},'.format(self.results['tester_host'],
                                             self.results['git_branch'],
                                             self.results['git_revision'],
                                             git_tag_rev))
            fobj.write(','.join(times + sizes) + '\n')


def perf_test_case(obj):
    """Decorator for adding test classes"""
    BuildPerfTestRunner.test_run_queue.append(obj)
    return obj


class BuildPerfTest(object):
    """Base class for build performance tests"""
    SYSRES = 'sysres'
    DISKUSAGE = 'diskusage'

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
        # TODO: remove the _failed flag when globalres.log is ditched as all
        # failures should raise an exception
        self._failed = False
        self.cmd_log = os.path.join(self.out_dir, 'commands.log')

    def run(self):
        """Run test"""
        self.results['status'] = 'FAILED'
        self.results['start_time'] = datetime.now()
        self._run()
        self.results['elapsed_time'] = (datetime.now() -
                                        self.results['start_time'])
        # Test is regarded as completed if it doesn't raise an exception
        if not self._failed:
            self.results['status'] = 'COMPLETED'

    def _run(self):
        """Actual test payload"""
        raise NotImplementedError

    def log_cmd_output(self, cmd):
        """Run a command and log it's output"""
        with open(self.cmd_log, 'a') as fobj:
            runCmd(cmd, stdout=fobj)

    def measure_cmd_resources(self, cmd, name, legend):
        """Measure system resource usage of a command"""
        def str_time_to_timedelta(strtime):
            """Convert time strig from the time utility to timedelta"""
            split = strtime.split(':')
            hours = int(split[0]) if len(split) > 2 else 0
            mins = int(split[-2])
            try:
                secs, frac = split[-1].split('.')
            except:
                secs = split[-1]
                frac = '0'
            secs = int(secs)
            microsecs = int(float('0.' + frac) * pow(10, 6))
            return timedelta(0, hours*3600 + mins*60 + secs, microsecs)

        cmd_str = cmd if isinstance(cmd, str) else ' '.join(cmd)
        log.info("Timing command: %s", cmd_str)
        with open(self.cmd_log, 'a') as fobj:
            ret, timedata = time_cmd(cmd, stdout=fobj)
        if ret.status:
            log.error("Time will be reported as 0. Command failed: %s",
                      ret.status)
            etime = timedelta(0)
            self._failed = True
        else:
            match = re.search(r'.*wall clock.*: (?P<etime>.*)\n', timedata)
            etime = str_time_to_timedelta(match.group('etime'))

        measurement = {'type': self.SYSRES,
                       'name': name,
                       'legend': legend}
        measurement['values'] = {'elapsed_time': etime}
        self.results['measurements'].append(measurement)
        nlogs = len(glob.glob(self.out_dir + '/results.log*'))
        results_log = os.path.join(self.out_dir,
                                   'results.log.{}'.format(nlogs + 1))
        with open(results_log, 'w') as fobj:
            fobj.write(timedata)

    def measure_disk_usage(self, path, name, legend):
        """Estimate disk usage of a file or directory"""
        # TODO: 'ignore_status' could/should be removed when globalres.log is
        # deprecated. The function would just raise an exception, instead
        ret = runCmd(['du', '-s', path], ignore_status=True)
        if ret.status:
            log.error("du failed, disk usage will be reported as 0")
            size = 0
            self._failed = True
        else:
            size = int(ret.output.split()[0])
            log.debug("Size of %s path is %s", path, size)
        measurement = {'type': self.DISKUSAGE,
                       'name': name,
                       'legend': legend}
        measurement['values'] = {'size': size}
        self.results['measurements'].append(measurement)

    def save_buildstats(self):
        """Save buildstats"""
        shutil.move(self.bb_vars['BUILDSTATS_BASE'],
                    os.path.join(self.out_dir, 'buildstats-' + self.name))

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
