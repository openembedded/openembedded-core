# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# Copyright (c) 2013, Intel Corporation.
# All rights reserved.
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
#
# DESCRIPTION
# This module provides a place to collect various wic-related utils
# for the OpenEmbedded Image Tools.
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#

from collections import defaultdict

from wic import msger
from wic.utils import runner

def __exec_cmd(cmd_and_args, as_shell=False, catch=3):
    """
    Execute command, catching stderr, stdout

    Need to execute as_shell if the command uses wildcards
    """
    msger.debug("__exec_cmd: %s" % cmd_and_args)
    args = cmd_and_args.split()
    msger.debug(args)

    if as_shell:
        rc, out = runner.runtool(cmd_and_args, catch)
    else:
        rc, out = runner.runtool(args, catch)
    out = out.strip()
    msger.debug("__exec_cmd: output for %s (rc = %d): %s" % \
                (cmd_and_args, rc, out))

    return (rc, out)


def exec_cmd(cmd_and_args, as_shell=False, catch=3):
    """
    Execute command, catching stderr, stdout

    Exits if rc non-zero
    """
    rc, out = __exec_cmd(cmd_and_args, as_shell, catch)

    if rc != 0:
        msger.error("exec_cmd: %s returned '%s' instead of 0" % (cmd_and_args, rc))

    return out


def exec_native_cmd(cmd_and_args, native_sysroot, catch=3):
    """
    Execute native command, catching stderr, stdout

    Need to execute as_shell if the command uses wildcards

    Always need to execute native commands as_shell
    """
    native_paths = \
        "export PATH=%s/sbin:%s/usr/sbin:%s/usr/bin" % \
        (native_sysroot, native_sysroot, native_sysroot)
    native_cmd_and_args = "%s;%s" % (native_paths, cmd_and_args)
    msger.debug("exec_native_cmd: %s" % cmd_and_args)

    args = cmd_and_args.split()
    msger.debug(args)

    rc, out = __exec_cmd(native_cmd_and_args, True, catch)

    if rc == 127: # shell command-not-found
        msger.error("A native program %s required to build the image "
                    "was not found (see details above). Please make sure "
                    "it's installed and try again." % args[0])
    if out:
        msger.debug('"%s" output: %s' % (args[0], out))

    if rc != 0:
        msger.error("exec_cmd: '%s' returned '%s' instead of 0" % \
                    (cmd_and_args, rc))

    return (rc, out)

# kickstart doesn't support variable substution in commands, so this
# is our current simplistic scheme for supporting that

wks_vars = dict()

def get_wks_var(key):
    return wks_vars[key]

def add_wks_var(key, val):
    wks_vars[key] = val

BOOTDD_EXTRA_SPACE = 16384

_BITBAKE_VARS = defaultdict(dict)

def get_bitbake_var(var, image=None):
    """
    Get bitbake variable value lazy way, i.e. run
    'bitbake -e' only when variable is requested.
    """
    if image not in _BITBAKE_VARS:
        # Get bitbake -e output
        cmd = "bitbake -e"
        if image:
            cmd += " %s" % image

        log_level = msger.get_loglevel()
        msger.set_loglevel('normal')
        rc, lines = __exec_cmd(cmd)
        msger.set_loglevel(log_level)

        if rc:
            print "Couldn't get '%s' output." % cmd
            print "Bitbake failed with error:\n%s\n" % lines
            return

        # Parse bitbake -e output
        for line in lines.split('\n'):
            if "=" not in line:
                continue
            try:
                key, val = line.split("=")
            except ValueError:
                continue
            key = key.strip()
            val = val.strip()
            if key.replace('_', '').isalnum():
                _BITBAKE_VARS[image][key] = val.strip('"')

        # Make first image a default set of variables
        images = [key for key in _BITBAKE_VARS if key]
        if len(images) == 1:
            _BITBAKE_VARS[None] = _BITBAKE_VARS[image]

    return _BITBAKE_VARS[image].get(var)

def parse_sourceparams(sourceparams):
    """
    Split sourceparams string of the form key1=val1[,key2=val2,...]
    into a dict.  Also accepts valueless keys i.e. without =.

    Returns dict of param key/val pairs (note that val may be None).
    """
    params_dict = {}

    params = sourceparams.split(',')
    if params:
        for p in params:
            if not p:
                continue
            if not '=' in p:
                key = p
                val = None
            else:
                key, val = p.split('=')
            params_dict[key] = val

    return params_dict
