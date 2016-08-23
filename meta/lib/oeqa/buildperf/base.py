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
import unittest
from datetime import datetime, timedelta
from functools import partial

import oe.path
from oeqa.utils.commands import CommandError, runCmd, get_bb_vars
from oeqa.utils.git import GitError, GitRepo

# Get logger for this module
log = logging.getLogger('build-perf')

# Our own version of runCmd which does not raise AssertErrors which would cause
# errors to interpreted as failures
runCmd2 = partial(runCmd, assert_error=False)


class KernelDropCaches(object):
    """Container of the functions for dropping kernel caches"""
    sudo_passwd = None

    @classmethod
    def check(cls):
        """Check permssions for dropping kernel caches"""
        from getpass import getpass
        from locale import getdefaultlocale
        cmd = ['sudo', '-k', '-n', 'tee', '/proc/sys/vm/drop_caches']
        ret = runCmd2(cmd, ignore_status=True, data=b'0')
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
        runCmd2(cmd, data=input_data)


def time_cmd(cmd, **kwargs):
    """TIme a command"""
    with tempfile.NamedTemporaryFile(mode='w+') as tmpf:
        timecmd = ['/usr/bin/time', '-v', '-o', tmpf.name]
        if isinstance(cmd, str):
            timecmd = ' '.join(timecmd) + ' '
        timecmd += cmd
        # TODO: 'ignore_status' could/should be removed when globalres.log is
        # deprecated. The function would just raise an exception, instead
        ret = runCmd2(timecmd, ignore_status=True, **kwargs)
        timedata = tmpf.file.read()
    return ret, timedata


class BuildPerfTestResult(unittest.TextTestResult):
    """Runner class for executing the individual tests"""
    # List of test cases to run
    test_run_queue = []

    def __init__(self, out_dir, *args, **kwargs):
        super(BuildPerfTestResult, self).__init__(*args, **kwargs)

        self.out_dir = out_dir
        # Get Git parameters
        try:
            self.repo = GitRepo('.')
        except GitError:
            self.repo = None
        self.git_commit, self.git_commit_count, self.git_branch = \
                self.get_git_revision()
        self.hostname = socket.gethostname()
        self.start_time = self.elapsed_time = None
        self.successes = []
        log.info("Using Git branch:commit %s:%s (%s)", self.git_branch,
                 self.git_commit, self.git_commit_count)

    def get_git_revision(self):
        """Get git branch and commit under testing"""
        commit = os.getenv('OE_BUILDPERFTEST_GIT_COMMIT')
        commit_cnt = os.getenv('OE_BUILDPERFTEST_GIT_COMMIT_COUNT')
        branch = os.getenv('OE_BUILDPERFTEST_GIT_BRANCH')
        if not self.repo and (not commit or not commit_cnt or not branch):
            log.info("The current working directory doesn't seem to be a Git "
                     "repository clone. You can specify branch and commit "
                     "displayed in test results with OE_BUILDPERFTEST_GIT_BRANCH, "
                     "OE_BUILDPERFTEST_GIT_COMMIT and "
                     "OE_BUILDPERFTEST_GIT_COMMIT_COUNT environment variables")
        else:
            if not commit:
                commit = self.repo.rev_parse('HEAD^0')
                commit_cnt = self.repo.run_cmd(['rev-list', '--count', 'HEAD^0'])
            if not branch:
                branch = self.repo.get_current_branch()
                if not branch:
                    log.debug('Currently on detached HEAD')
        return str(commit), str(commit_cnt), str(branch)

    def addSuccess(self, test):
        """Record results from successful tests"""
        super(BuildPerfTestResult, self).addSuccess(test)
        self.successes.append((test, None))

    def startTest(self, test):
        """Pre-test hook"""
        test.out_dir = self.out_dir
        log.info("Executing test %s: %s", test.name, test.shortDescription())
        self.stream.write(datetime.now().strftime("[%Y-%m-%d %H:%M:%S] "))
        super(BuildPerfTestResult, self).startTest(test)

    def startTestRun(self):
        """Pre-run hook"""
        self.start_time = datetime.utcnow()

    def stopTestRun(self):
        """Pre-run hook"""
        self.elapsed_time = datetime.utcnow() - self.start_time

    def all_results(self):
        result_map = {'SUCCESS': self.successes,
                      'FAIL': self.failures,
                      'ERROR': self.errors,
                      'EXP_FAIL': self.expectedFailures,
                      'UNEXP_SUCCESS': self.unexpectedSuccesses}
        for status, tests in result_map.items():
            for test in tests:
                yield (status, test)


    def update_globalres_file(self, filename):
        """Write results to globalres csv file"""
        # Map test names to time and size columns in globalres
        # The tuples represent index and length of times and sizes
        # respectively
        gr_map = {'test1': ((0, 1), (8, 1)),
                  'test12': ((1, 1), (None, None)),
                  'test13': ((2, 1), (9, 1)),
                  'test2': ((3, 1), (None, None)),
                  'test3': ((4, 3), (None, None)),
                  'test4': ((7, 1), (10, 2))}

        if self.repo:
            git_tag_rev = self.repo.run_cmd(['describe', self.git_commit])
        else:
            git_tag_rev = self.git_commit

        values = ['0'] * 12
        for status, (test, msg) in self.all_results():
            if status not in ['SUCCESS', 'FAILURE', 'EXP_SUCCESS']:
                continue
            (t_ind, t_len), (s_ind, s_len) = gr_map[test.name]
            if t_ind is not None:
                values[t_ind:t_ind + t_len] = test.times
            if s_ind is not None:
                values[s_ind:s_ind + s_len] = test.sizes

        log.debug("Writing globalres log to %s", filename)
        with open(filename, 'a') as fobj:
            fobj.write('{},{}:{},{},'.format(self.hostname,
                                             self.git_branch,
                                             self.git_commit,
                                             git_tag_rev))
            fobj.write(','.join(values) + '\n')


    def git_commit_results(self, repo_path, branch=None, tag=None):
        """Commit results into a Git repository"""
        repo = GitRepo(repo_path, is_topdir=True)
        if not branch:
            branch = self.git_branch
        else:
            # Replace keywords
            branch = branch.format(git_branch=self.git_branch,
                                   tester_host=self.hostname)

        log.info("Committing test results into %s %s", repo_path, branch)
        tmp_index = os.path.join(repo_path, '.git', 'index.oe-build-perf')
        try:
            # Create new commit object from the new results
            env_update = {'GIT_INDEX_FILE': tmp_index,
                          'GIT_WORK_TREE': self.out_dir}
            repo.run_cmd('add .', env_update)
            tree = repo.run_cmd('write-tree', env_update)
            parent = repo.rev_parse(branch)
            msg = "Results of {}:{}\n".format(self.git_branch, self.git_commit)
            git_cmd = ['commit-tree', tree, '-m', msg]
            if parent:
                git_cmd += ['-p', parent]
            commit = repo.run_cmd(git_cmd, env_update)

            # Update branch head
            git_cmd = ['update-ref', 'refs/heads/' + branch, commit]
            if parent:
                git_cmd.append(parent)
            repo.run_cmd(git_cmd)

            # Update current HEAD, if we're on branch 'branch'
            if repo.get_current_branch() == branch:
                log.info("Updating %s HEAD to latest commit", repo_path)
                repo.run_cmd('reset --hard')

            # Create (annotated) tag
            if tag:
                # Find tags matching the pattern
                tag_keywords = dict(git_branch=self.git_branch,
                                    git_commit=self.git_commit,
                                    git_commit_count=self.git_commit_count,
                                    tester_host=self.hostname,
                                    tag_num='[0-9]{1,5}')
                tag_re = re.compile(tag.format(**tag_keywords) + '$')
                tag_keywords['tag_num'] = 0
                for existing_tag in repo.run_cmd('tag').splitlines():
                    if tag_re.match(existing_tag):
                        tag_keywords['tag_num'] += 1

                tag = tag.format(**tag_keywords)
                msg = "Test run #{} of {}:{}\n".format(tag_keywords['tag_num'],
                                                       self.git_branch,
                                                       self.git_commit)
                repo.run_cmd(['tag', '-a', '-m', msg, tag, commit])

        finally:
            if os.path.exists(tmp_index):
                os.unlink(tmp_index)


class BuildPerfTestCase(unittest.TestCase):
    """Base class for build performance tests"""
    SYSRES = 'sysres'
    DISKUSAGE = 'diskusage'

    def __init__(self, *args, **kwargs):
        super(BuildPerfTestCase, self).__init__(*args, **kwargs)
        self.name = self._testMethodName
        self.out_dir = None
        self.start_time = None
        self.elapsed_time = None
        self.measurements = []
        self.bb_vars = get_bb_vars()
        # TODO: remove 'times' and 'sizes' arrays when globalres support is
        # removed
        self.times = []
        self.sizes = []

    def run(self, *args, **kwargs):
        """Run test"""
        self.start_time = datetime.now()
        super(BuildPerfTestCase, self).run(*args, **kwargs)
        self.elapsed_time = datetime.now() - self.start_time

    def log_cmd_output(self, cmd):
        """Run a command and log it's output"""
        cmd_str = cmd if isinstance(cmd, str) else ' '.join(cmd)
        log.info("Logging command: %s", cmd_str)
        cmd_log = os.path.join(self.out_dir, 'commands.log')
        try:
            with open(cmd_log, 'a') as fobj:
                runCmd2(cmd, stdout=fobj)
        except CommandError as err:
            log.error("Command failed: %s", err.retcode)
            raise

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
        cmd_log = os.path.join(self.out_dir, 'commands.log')
        with open(cmd_log, 'a') as fobj:
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
        self.measurements.append(measurement)
        e_sec = etime.total_seconds()
        nlogs = len(glob.glob(self.out_dir + '/results.log*'))
        results_log = os.path.join(self.out_dir,
                                   'results.log.{}'.format(nlogs + 1))
        with open(results_log, 'w') as fobj:
            fobj.write(timedata)
        # Append to 'times' array for globalres log
        self.times.append('{:d}:{:02d}:{:.2f}'.format(int(e_sec / 3600),
                                                      int((e_sec % 3600) / 60),
                                                       e_sec % 60))

    def measure_disk_usage(self, path, name, legend):
        """Estimate disk usage of a file or directory"""
        # TODO: 'ignore_status' could/should be removed when globalres.log is
        # deprecated. The function would just raise an exception, instead
        ret = runCmd2(['du', '-s', path], ignore_status=True)
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
        self.measurements.append(measurement)
        # Append to 'sizes' array for globalres log
        self.sizes.append(str(size))

    def save_buildstats(self):
        """Save buildstats"""
        shutil.move(self.bb_vars['BUILDSTATS_BASE'],
                    os.path.join(self.out_dir, 'buildstats-' + self.name))

    def rm_tmp(self):
        """Cleanup temporary/intermediate files and directories"""
        log.debug("Removing temporary and cache files")
        for name in ['bitbake.lock', 'conf/sanity_info',
                     self.bb_vars['TMPDIR']]:
            oe.path.remove(name, recurse=True)

    def rm_sstate(self):
        """Remove sstate directory"""
        log.debug("Removing sstate-cache")
        oe.path.remove(self.bb_vars['SSTATE_DIR'], recurse=True)

    def rm_cache(self):
        """Drop bitbake caches"""
        oe.path.remove(self.bb_vars['PERSISTENT_DIR'], recurse=True)

    @staticmethod
    def sync():
        """Sync and drop kernel caches"""
        log.debug("Syncing and dropping kernel caches""")
        KernelDropCaches.drop()
        os.sync()
        # Wait a bit for all the dirty blocks to be written onto disk
        time.sleep(3)


class BuildPerfTestLoader(unittest.TestLoader):
    """Test loader for build performance tests"""
    sortTestMethodsUsing = None


class BuildPerfTestRunner(unittest.TextTestRunner):
    """Test loader for build performance tests"""
    sortTestMethodsUsing = None

    def __init__(self, out_dir, *args, **kwargs):
        super(BuildPerfTestRunner, self).__init__(*args, **kwargs)
        self.out_dir = out_dir

    def _makeResult(self):
       return BuildPerfTestResult(self.out_dir, self.stream, self.descriptions,
                                  self.verbosity)
