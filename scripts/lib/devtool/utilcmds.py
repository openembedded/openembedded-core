# Development tool - utility commands plugin
#
# Copyright (C) 2015-2016 Intel Corporation
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

"""Devtool utility plugins"""

import os
import sys
import shutil
import tempfile
import logging
import argparse
import subprocess
from devtool import exec_build_env_command, setup_tinfoil, check_workspace_recipe, DevtoolError
from devtool import parse_recipe

logger = logging.getLogger('devtool')


def edit_recipe(args, config, basepath, workspace):
    """Entry point for the devtool 'edit-recipe' subcommand"""
    if args.any_recipe:
        tinfoil = setup_tinfoil(config_only=False, basepath=basepath)
        try:
            rd = parse_recipe(config, tinfoil, args.recipename, True)
            if not rd:
                return 1
            recipefile = rd.getVar('FILE', True)
        finally:
            tinfoil.shutdown()
    else:
        check_workspace_recipe(workspace, args.recipename)
        recipefile = workspace[args.recipename]['recipefile']
        if not recipefile:
            raise DevtoolError("Recipe file for %s is not under the workspace" %
                               args.recipename)

    editor = os.environ.get('EDITOR', None)
    if not editor:
        raise DevtoolError("EDITOR environment variable not set")

    import subprocess
    try:
        subprocess.check_call('%s "%s"' % (editor, recipefile), shell=True)
    except subprocess.CalledProcessError as e:
        return e.returncode

    return 0


def register_commands(subparsers, context):
    """Register devtool subcommands from this plugin"""
    parser_edit_recipe = subparsers.add_parser('edit-recipe', help='Edit a recipe file in your workspace',
                                         description='Runs the default editor (as specified by the EDITOR variable) on the specified recipe. Note that the recipe file itself must be in the workspace (i.e. as a result of "devtool add" or "devtool upgrade"); you can override this with the -a/--any-recipe option.')
    parser_edit_recipe.add_argument('recipename', help='Recipe to edit')
    parser_edit_recipe.add_argument('--any-recipe', '-a', action="store_true", help='Edit any recipe, not just where the recipe file itself is in the workspace')
    parser_edit_recipe.set_defaults(func=edit_recipe)
