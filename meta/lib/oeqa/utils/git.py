#
# Copyright (C) 2016 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)
#
"""Git repository interactions"""
from oeqa.utils.commands import runCmd


class GitError(Exception):
    """Git error handling"""
    pass

class GitRepo(object):
    """Class representing a Git repository clone"""
    def __init__(self, cwd):
        self.top_dir = self._run_git_cmd_at(['rev-parse', '--show-toplevel'],
                                            cwd)

    @staticmethod
    def _run_git_cmd_at(git_args, cwd, **kwargs):
        """Run git command at a specified directory"""
        git_cmd = 'git ' if isinstance(git_args, str) else ['git']
        git_cmd += git_args
        ret = runCmd(git_cmd, ignore_status=True, cwd=cwd, **kwargs)
        if ret.status:
            cmd_str = git_cmd if isinstance(git_cmd, str) \
                                else ' '.join(git_cmd)
            raise GitError("'{}' failed with exit code {}: {}".format(
                cmd_str, ret.status, ret.output))
        return ret.output.strip()

    def run_cmd(self, git_args):
        """Run Git command"""
        return self._run_git_cmd_at(git_args, self.top_dir)



