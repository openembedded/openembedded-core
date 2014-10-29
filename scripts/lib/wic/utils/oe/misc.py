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

from wic import msger
from wic.utils import runner

def __exec_cmd(cmd_and_args, as_shell = False, catch = 3):
    """
    Execute command, catching stderr, stdout

    Need to execute as_shell if the command uses wildcards
    """
    msger.debug("__exec_cmd: %s" % cmd_and_args)
    args = cmd_and_args.split()
    msger.debug(args)

    if (as_shell):
        rc, out = runner.runtool(cmd_and_args, catch)
    else:
        rc, out = runner.runtool(args, catch)
    out = out.strip()
    msger.debug("__exec_cmd: output for %s (rc = %d): %s" % \
                (cmd_and_args, rc, out))

    return (rc, out)


def exec_cmd(cmd_and_args, as_shell = False, catch = 3):
    """
    Execute command, catching stderr, stdout

    Exits if rc non-zero
    """
    rc, out = __exec_cmd(cmd_and_args, as_shell, catch)

    if rc != 0:
        msger.error("exec_cmd: %s returned '%s' instead of 0" % (cmd_and_args, rc))

    return out


def exec_cmd_quiet(cmd_and_args, as_shell = False):
    """
    Execute command, catching nothing in the output

    Exits if rc non-zero
    """
    return exec_cmd(cmd_and_args, as_shell, 0)


def exec_native_cmd(cmd_and_args, native_sysroot, catch = 3):
    """
    Execute native command, catching stderr, stdout

    Need to execute as_shell if the command uses wildcards

    Always need to execute native commands as_shell
    """
    native_paths = \
        "export PATH=%s/sbin:%s/usr/sbin:%s/usr/bin:$PATH" % \
        (native_sysroot, native_sysroot, native_sysroot)
    native_cmd_and_args = "%s;%s" % (native_paths, cmd_and_args)
    msger.debug("exec_native_cmd: %s" % cmd_and_args)

    args = cmd_and_args.split()
    msger.debug(args)

    rc, out = __exec_cmd(native_cmd_and_args, True, catch)

    if rc == 127: # shell command-not-found
        msger.error("A native (host) program required to build the image "
                    "was not found (see details above). Please make sure "
                    "it's installed and try again.")

    return (rc, out)


def exec_native_cmd_quiet(cmd_and_args, native_sysroot):
    """
    Execute native command, catching nothing in the output

    Need to execute as_shell if the command uses wildcards

    Always need to execute native commands as_shell
    """
    return exec_native_cmd(cmd_and_args, native_sysroot, 0)


# kickstart doesn't support variable substution in commands, so this
# is our current simplistic scheme for supporting that

wks_vars = dict()

def get_wks_var(key):
    return wks_vars[key]

def add_wks_var(key, val):
    wks_vars[key] = val

BOOTDD_EXTRA_SPACE = 16384
IMAGE_EXTRA_SPACE = 10240
IMAGE_OVERHEAD_FACTOR = 1.3

__bitbake_env_lines = ""

def set_bitbake_env_lines(bitbake_env_lines):
    global __bitbake_env_lines
    __bitbake_env_lines = bitbake_env_lines

def get_bitbake_env_lines():
    return __bitbake_env_lines

def find_bitbake_env_lines(image_name):
    """
    If image_name is empty, plugins might still be able to use the
    environment, so set it regardless.
    """
    if image_name:
        bitbake_env_cmd = "bitbake -e %s" % image_name
    else:
        bitbake_env_cmd = "bitbake -e"
    rc, bitbake_env_lines = __exec_cmd(bitbake_env_cmd)
    if rc != 0:
        print "Couldn't get '%s' output." % bitbake_env_cmd
        return None

    return bitbake_env_lines

def find_artifact(bitbake_env_lines, variable):
    """
    Gather the build artifact for the current image (the image_name
    e.g. core-image-minimal) for the current MACHINE set in local.conf
    """
    retval = ""

    for line in bitbake_env_lines.split('\n'):
        if (get_line_val(line, variable)):
            retval = get_line_val(line, variable)
            break

    return retval

def get_line_val(line, key):
    """
    Extract the value from the VAR="val" string
    """
    if line.startswith(key + "="):
        stripped_line = line.split('=')[1]
        stripped_line = stripped_line.replace('\"', '')
        return stripped_line
    return None

def get_bitbake_var(key):
    for line in __bitbake_env_lines.split('\n'):
        if (get_line_val(line, key)):
            val = get_line_val(line, key)
            return val
    return None

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
