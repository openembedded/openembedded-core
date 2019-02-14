# test result tool - utilities
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
import os
import json
import scriptpath
scriptpath.add_oe_lib_path()
from oeqa.utils.git import GitRepo, GitError

def load_json_file(file):
    with open(file, "r") as f:
        return json.load(f)

def dump_json_data(write_dir, file_name, json_data):
    file_content = json.dumps(json_data, sort_keys=True, indent=4)
    file_path = os.path.join(write_dir, file_name)
    with open(file_path, 'w') as the_file:
        the_file.write(file_content)

def get_dict_value(logger, dict, key):
    try:
        return dict[key]
    except KeyError:
        if logger:
            logger.debug('Faced KeyError exception: dict=%s: key=%s' % (dict, key))
        return None
    except TypeError:
        if logger:
            logger.debug('Faced TypeError exception: dict=%s: key=%s' % (dict, key))
        return None

def pop_dict_element(logger, dict, key):
    try:
        dict.pop(key)
    except KeyError:
        if logger:
            logger.debug('Faced KeyError exception: dict=%s: key=%s' % (dict, key))
    except AttributeError:
        if logger:
            logger.debug('Faced AttributeError exception: dict=%s: key=%s' % (dict, key))

def checkout_git_dir(git_dir, git_branch):
    try:
        repo = GitRepo(git_dir, is_topdir=True)
        repo.run_cmd('checkout %s' % git_branch)
        return True
    except GitError:
        return False

def get_directory_files(source_dir, excludes, file):
    files_in_dir = []
    for root, dirs, files in os.walk(source_dir, topdown=True):
        [dirs.remove(d) for d in list(dirs) if d in excludes]
        for name in files:
            if name == file:
                files_in_dir.append(os.path.join(root, name))
    return files_in_dir