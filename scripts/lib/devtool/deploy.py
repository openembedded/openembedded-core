# Development tool - deploy/undeploy command plugin
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

import os
import subprocess
import logging
from devtool import exec_build_env_command

logger = logging.getLogger('devtool')

def plugin_init(pluginlist):
    pass


def deploy(args, config, basepath, workspace):
    import re
    from devtool import exec_build_env_command

    if not args.recipename in workspace:
        logger.error("no recipe named %s in your workspace" % args.recipename)
        return -1
    try:
        host, destdir = args.target.split(':')
    except ValueError:
        destdir = '/'
    else:
        args.target = host

    deploy_dir = os.path.join(basepath, 'target_deploy', args.target)
    deploy_file = os.path.join(deploy_dir, args.recipename + '.list')

    if os.path.exists(deploy_file):
        if undeploy(args, config, basepath, workspace):
            # Error already shown
            return -1

    stdout, stderr = exec_build_env_command(config.init_path, basepath, 'bitbake -e %s' % args.recipename, shell=True)
    recipe_outdir = re.search(r'^D="(.*)"', stdout, re.MULTILINE).group(1)
    extraoptions = ''
    if args.no_host_check:
        extraoptions += '-o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no'
    ret = subprocess.call('scp -qr %s %s/* %s:%s' % (extraoptions, recipe_outdir, args.target, destdir), shell=True)
    if ret != 0:
        return ret

    logger.info('Successfully deployed %s' % recipe_outdir)

    if not os.path.exists(deploy_dir):
        os.makedirs(deploy_dir)

    files_list = []
    for root, _, files in os.walk(recipe_outdir):
        for filename in files:
            filename = os.path.relpath(os.path.join(root, filename), recipe_outdir)
            files_list.append(os.path.join(destdir, filename))

    with open(deploy_file, 'w') as fobj:
        fobj.write('\n'.join(files_list))

    return 0

def undeploy(args, config, basepath, workspace):

    deploy_file = os.path.join(basepath, 'target_deploy', args.target, args.recipename + '.list')
    if not os.path.exists(deploy_file):
         logger.error('%s has not been deployed' % args.recipename)
         return -1

    extraoptions = ''
    if args.no_host_check:
        extraoptions += '-o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no'

    ret = subprocess.call("scp -q %s %s %s:/tmp" % (extraoptions, deploy_file, args.target), shell=True)
    if ret != 0:
        logger.error('Failed to copy %s to %s' % (deploy, args.target))
        return -1

    ret = subprocess.call("ssh %s %s 'xargs -n1 rm -f </tmp/%s'" % (extraoptions, args.target, os.path.basename(deploy_file)), shell=True)
    if ret == 0:
        logger.info('Successfully undeployed %s' % args.recipename)
        os.remove(deploy_file)

    return ret


def register_commands(subparsers, context):
    parser_deploy = subparsers.add_parser('deploy-target', help='Deploy recipe output files to live target machine')
    parser_deploy.add_argument('recipename', help='Recipe to deploy')
    parser_deploy.add_argument('target', help='Live target machine running an ssh server: user@hostname[:destdir]')
    parser_deploy.add_argument('-c', '--no-host-check', help='Disable ssh host key checking', action='store_true')
    parser_deploy.set_defaults(func=deploy)

    parser_undeploy = subparsers.add_parser('undeploy-target', help='Undeploy recipe output files in live target machine')
    parser_undeploy.add_argument('recipename', help='Recipe to undeploy')
    parser_undeploy.add_argument('target', help='Live target machine running an ssh server: user@hostname')
    parser_undeploy.add_argument('-c', '--no-host-check', help='Disable ssh host key checking', action='store_true')
    parser_undeploy.set_defaults(func=undeploy)
