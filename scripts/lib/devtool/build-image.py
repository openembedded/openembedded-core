# Development tool - build-image plugin
#
# Copyright (C) 2015 Intel Corporation
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

"""Devtool plugin containing the build-image subcommand."""

import os
import logging

from bb.process import ExecutionError
from devtool import exec_build_env_command

logger = logging.getLogger('devtool')

def plugin_init(pluginlist):
    """Plugin initialization"""
    pass

def build_image(args, config, basepath, workspace):
    """Entry point for the devtool 'build-image' subcommand."""
    image = args.recipe
    appendfile = os.path.join(config.workspace_path, 'appends',
                              '%s.bbappend' % image)
    with open(appendfile, 'w') as afile:
        afile.write('IMAGE_INSTALL_append = " %s"\n' % \
                    ' '.join(workspace.keys()))

    try:
        exec_build_env_command(config.init_path, basepath,
                               'bitbake %s' % image, watch=True)
    except ExecutionError as err:
        return err.exitcode

    logger.info('Successfully built %s', image)

def register_commands(subparsers, context):
    """Register devtool subcommands from the build-image plugin"""
    parser_package = subparsers.add_parser('build-image', help='Build image')
    parser_package.add_argument('recipe', help='Image recipe to build')
    parser_package.set_defaults(func=build_image)

