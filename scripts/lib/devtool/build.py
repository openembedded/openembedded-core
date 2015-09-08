# Development tool - build command plugin
#
# Copyright (C) 2014-2015 Intel Corporation
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
"""Devtool build plugin"""

import logging
import argparse
from devtool import exec_build_env_command

logger = logging.getLogger('devtool')

def plugin_init(pluginlist):
    """Plugin initialization"""
    pass

def build(args, config, basepath, workspace):
    """Entry point for the devtool 'build' subcommand"""
    import bb
    if not args.recipename in workspace:
        raise DevtoolError("no recipe named %s in your workspace" %
                           args.recipename)
    build_task = config.get('Build', 'build_task', 'populate_sysroot')
    try:
        exec_build_env_command(config.init_path, basepath, 'bitbake -c %s %s' % (build_task, args.recipename), watch=True)
    except bb.process.ExecutionError as e:
        # We've already seen the output since watch=True, so just ensure we return something to the user
        return e.exitcode

    return 0

def register_commands(subparsers, context):
    """Register devtool subcommands from this plugin"""
    parser_build = subparsers.add_parser('build', help='Build a recipe',
                                         description='Builds the specified recipe using bitbake',
                                         formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser_build.add_argument('recipename', help='Recipe to build')
    parser_build.set_defaults(func=build)
