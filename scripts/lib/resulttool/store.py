# test result tool - store test results
#
# Copyright (c) 2019, Intel Corporation.
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
import datetime
import tempfile
import os
import subprocess
import scriptpath
scriptpath.add_bitbake_lib_path()
scriptpath.add_oe_lib_path()
from resulttool.resultsutils import checkout_git_dir
try:
    import bb
except ImportError:
    pass

class ResultsGitStore(object):

    def _get_output_dir(self):
        basepath = os.environ['BUILDDIR']
        return basepath + '/testresults_%s/' % datetime.datetime.now().strftime("%Y%m%d%H%M%S")

    def _create_temporary_workspace_dir(self):
        return tempfile.mkdtemp(prefix='testresults.')

    def _remove_temporary_workspace_dir(self, workspace_dir):
        return subprocess.run(["rm", "-rf",  workspace_dir])

    def _oe_copy_files(self, source_dir, destination_dir):
        from oe.path import copytree
        copytree(source_dir, destination_dir)

    def _copy_files(self, source_dir, destination_dir, copy_ignore=None):
        from shutil import copytree
        copytree(source_dir, destination_dir, ignore=copy_ignore)

    def _store_files_to_git(self, logger, file_dir, git_dir, git_branch, commit_msg_subject, commit_msg_body):
        logger.debug('Storing test result into git repository (%s) and branch (%s)'
                     % (git_dir, git_branch))
        return subprocess.run(["oe-git-archive",
                               file_dir,
                               "-g", git_dir,
                               "-b", git_branch,
                               "--commit-msg-subject", commit_msg_subject,
                               "--commit-msg-body", commit_msg_body])

    def store_to_existing(self, logger, source_dir, git_dir, git_branch):
        logger.debug('Storing files to existing git repository and branch')
        from shutil import ignore_patterns
        dest_dir = self._create_temporary_workspace_dir()
        dest_top_dir = os.path.join(dest_dir, 'top_dir')
        self._copy_files(git_dir, dest_top_dir, copy_ignore=ignore_patterns('.git'))
        self._oe_copy_files(source_dir, dest_top_dir)
        self._store_files_to_git(logger, dest_top_dir, git_dir, git_branch,
                                 'Store as existing git and branch', 'Store as existing git repository and branch')
        self._remove_temporary_workspace_dir(dest_dir)
        return git_dir

    def store_to_existing_with_new_branch(self, logger, source_dir, git_dir, git_branch):
        logger.debug('Storing files to existing git repository with new branch')
        self._store_files_to_git(logger, source_dir, git_dir, git_branch,
                                 'Store as existing git with new branch',
                                 'Store as existing git repository with new branch')
        return git_dir

    def store_to_new(self, logger, source_dir, git_branch):
        logger.debug('Storing files to new git repository')
        output_dir = self._get_output_dir()
        self._store_files_to_git(logger, source_dir, output_dir, git_branch,
                                 'Store as new', 'Store as new git repository')
        return output_dir

    def store(self, logger, source_dir, git_dir, git_branch):
        if git_dir:
            if checkout_git_dir(git_dir, git_branch):
                self.store_to_existing(logger, source_dir, git_dir, git_branch)
            else:
                self.store_to_existing_with_new_branch(logger, source_dir, git_dir, git_branch)
        else:
            self.store_to_new(logger, source_dir, git_branch)

def store(args, logger):
    gitstore = ResultsGitStore()
    gitstore.store(logger, args.source_dir, args.git_dir, args.git_branch)
    return 0

def register_commands(subparsers):
    """Register subcommands from this plugin"""
    parser_build = subparsers.add_parser('store', help='store test result files and directories into git repository',
                                         description='store the testresults.json files and related directories '
                                                     'from the source directory into the destination git repository '
                                                     'with the given git branch',
                                         group='setup')
    parser_build.set_defaults(func=store)
    parser_build.add_argument('source_dir',
                              help='source directory that contain the test result files and directories to be stored')
    parser_build.add_argument('git_branch', help='git branch used for store')
    parser_build.add_argument('-d', '--git-dir', default='',
                              help='(optional) default store to new <top_dir>/<build>/<testresults_datetime> '
                                   'directory unless provided with existing git repository as destination')
