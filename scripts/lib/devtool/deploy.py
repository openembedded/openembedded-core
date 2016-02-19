# Development tool - deploy/undeploy command plugin
#
# Copyright (C) 2014-2016 Intel Corporation
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
"""Devtool plugin containing the deploy subcommands"""

import os
import subprocess
import logging
import tempfile
import shutil
from devtool import exec_fakeroot, setup_tinfoil, check_workspace_recipe, DevtoolError

logger = logging.getLogger('devtool')

deploylist_path = '/.devtool'

def _prepare_remote_script(deploy, verbose=False):
    """
    Prepare a shell script for running on the target to
    deploy/undeploy files. We have to be careful what we put in this
    script - only commands that are likely to be available on the
    target are suitable (the target might be constrained, e.g. using
    busybox rather than bash with coreutils).
    """
    lines = []
    lines.append('#!/bin/sh')
    lines.append('set -e')
    lines.append('manifest="%s/$1.list"' % deploylist_path)
    lines.append('if [ -f $manifest ] ; then')
    # Read manifest in reverse and delete files / remove empty dirs
    lines.append('    sed \'1!G;h;$!d\' $manifest | while read file')
    lines.append('    do')
    lines.append('        if [ -d $file ] ; then')
    lines.append('            rmdir $file > /dev/null 2>&1 || true')
    lines.append('        else')
    lines.append('            rm $file')
    lines.append('        fi')
    lines.append('    done')
    lines.append('    rm $manifest')
    if not deploy:
        # May as well remove all traces
        lines.append('    rmdir `dirname $manifest` > /dev/null 2>&1 || true')
    lines.append('fi')

    if deploy:
        lines.append('mkdir -p `dirname $manifest`')
        lines.append('mkdir -p $2')
        if verbose:
            lines.append('    tar xv -C $2 -f - | tee $manifest')
        else:
            lines.append('    tar xv -C $2 -f - > $manifest')
        lines.append('sed -i "s!^./!$2!" $manifest')
    # Delete the script itself
    lines.append('rm $0')
    lines.append('')

    return '\n'.join(lines)


def deploy(args, config, basepath, workspace):
    """Entry point for the devtool 'deploy' subcommand"""
    import re
    import oe.recipeutils

    check_workspace_recipe(workspace, args.recipename, checksrc=False)

    try:
        host, destdir = args.target.split(':')
    except ValueError:
        destdir = '/'
    else:
        args.target = host
    if not destdir.endswith('/'):
        destdir += '/'

    tinfoil = setup_tinfoil(basepath=basepath)
    try:
        rd = oe.recipeutils.parse_recipe_simple(tinfoil.cooker, args.recipename, tinfoil.config_data)
    except Exception as e:
        raise DevtoolError('Exception parsing recipe %s: %s' %
                           (args.recipename, e))
    recipe_outdir = rd.getVar('D', True)
    if not os.path.exists(recipe_outdir) or not os.listdir(recipe_outdir):
        raise DevtoolError('No files to deploy - have you built the %s '
                           'recipe? If so, the install step has not installed '
                           'any files.' % args.recipename)

    if args.dry_run:
        print('Files to be deployed for %s on target %s:' % (args.recipename, args.target))
        for root, _, files in os.walk(recipe_outdir):
            for fn in files:
                print('  %s' % os.path.join(destdir, os.path.relpath(root, recipe_outdir), fn))
        return 0


    extraoptions = ''
    if args.no_host_check:
        extraoptions += '-o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no'
    if not args.show_status:
        extraoptions += ' -q'

    # In order to delete previously deployed files and have the manifest file on
    # the target, we write out a shell script and then copy it to the target
    # so we can then run it (piping tar output to it).
    # (We cannot use scp here, because it doesn't preserve symlinks.)
    tmpdir = tempfile.mkdtemp(prefix='devtool')
    try:
        tmpscript = '/tmp/devtool_deploy.sh'
        shellscript = _prepare_remote_script(deploy=True, verbose=args.show_status)
        # Write out the script to a file
        with open(os.path.join(tmpdir, os.path.basename(tmpscript)), 'w') as f:
            f.write(shellscript)
        # Copy it to the target
        ret = subprocess.call("scp %s %s/* %s:%s" % (extraoptions, tmpdir, args.target, os.path.dirname(tmpscript)), shell=True)
        if ret != 0:
            raise DevtoolError('Failed to copy script to %s - rerun with -s to '
                            'get a complete error message' % args.target)
    finally:
        shutil.rmtree(tmpdir)

    # Now run the script
    ret = exec_fakeroot(rd, 'tar cf - . | ssh %s %s \'sh %s %s %s\'' % (extraoptions, args.target, tmpscript, args.recipename, destdir), cwd=recipe_outdir, shell=True)
    if ret != 0:
        raise DevtoolError('Deploy failed - rerun with -s to get a complete '
                           'error message')

    logger.info('Successfully deployed %s' % recipe_outdir)

    files_list = []
    for root, _, files in os.walk(recipe_outdir):
        for filename in files:
            filename = os.path.relpath(os.path.join(root, filename), recipe_outdir)
            files_list.append(os.path.join(destdir, filename))

    return 0

def undeploy(args, config, basepath, workspace):
    """Entry point for the devtool 'undeploy' subcommand"""
    extraoptions = ''
    if args.no_host_check:
        extraoptions += '-o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no'
    if not args.show_status:
        extraoptions += ' -q'

    args.target = args.target.split(':')[0]

    if args.dry_run:
        listfile = os.path.join(deploylist_path, '%s.list' % args.recipename)
        print('Previously deployed files to be un-deployed for %s on target %s:' % (args.recipename, args.target))
        ret = subprocess.call('ssh %s %s \'[ -f %s ] && cat %s || true\'' % (extraoptions, args.target, listfile, listfile), shell=True)
        if ret != 0:
            raise DevtoolError('Undeploy failed - rerun with -s to get a complete '
                               'error message')
        return 0

    tmpdir = tempfile.mkdtemp(prefix='devtool')
    try:
        tmpscript = '/tmp/devtool_undeploy.sh'
        shellscript = _prepare_remote_script(deploy=False)
        # Write out the script to a file
        with open(os.path.join(tmpdir, os.path.basename(tmpscript)), 'w') as f:
            f.write(shellscript)
        # Copy it to the target
        ret = subprocess.call("scp %s %s/* %s:%s" % (extraoptions, tmpdir, args.target, os.path.dirname(tmpscript)), shell=True)
        if ret != 0:
            raise DevtoolError('Failed to copy script to %s - rerun with -s to '
                                'get a complete error message' % args.target)
    finally:
        shutil.rmtree(tmpdir)

    # Now run the script
    ret = subprocess.call('ssh %s %s \'sh %s %s\'' % (extraoptions, args.target, tmpscript, args.recipename), shell=True)
    if ret != 0:
        raise DevtoolError('Undeploy failed - rerun with -s to get a complete '
                           'error message')

    logger.info('Successfully undeployed %s' % args.recipename)
    return 0


def register_commands(subparsers, context):
    """Register devtool subcommands from the deploy plugin"""
    parser_deploy = subparsers.add_parser('deploy-target',
                                          help='Deploy recipe output files to live target machine',
                                          description='Deploys a recipe\'s build output (i.e. the output of the do_install task) to a live target machine over ssh. Note: this only deploys the recipe itself and not any runtime dependencies, so it is assumed that those have been installed on the target beforehand.',
                                          group='testbuild')
    parser_deploy.add_argument('recipename', help='Recipe to deploy')
    parser_deploy.add_argument('target', help='Live target machine running an ssh server: user@hostname[:destdir]')
    parser_deploy.add_argument('-c', '--no-host-check', help='Disable ssh host key checking', action='store_true')
    parser_deploy.add_argument('-s', '--show-status', help='Show progress/status output', action='store_true')
    parser_deploy.add_argument('-n', '--dry-run', help='List files to be deployed only', action='store_true')
    parser_deploy.set_defaults(func=deploy)

    parser_undeploy = subparsers.add_parser('undeploy-target',
                                            help='Undeploy recipe output files in live target machine',
                                            description='Un-deploys recipe output files previously deployed to a live target machine by devtool deploy-target.',
                                            group='testbuild')
    parser_undeploy.add_argument('recipename', help='Recipe to undeploy')
    parser_undeploy.add_argument('target', help='Live target machine running an ssh server: user@hostname')
    parser_undeploy.add_argument('-c', '--no-host-check', help='Disable ssh host key checking', action='store_true')
    parser_undeploy.add_argument('-s', '--show-status', help='Show progress/status output', action='store_true')
    parser_undeploy.add_argument('-n', '--dry-run', help='List files to be undeployed only', action='store_true')
    parser_undeploy.set_defaults(func=undeploy)
