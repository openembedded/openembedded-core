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
# This module provides a place to collect various mic-related utils
# for the OpenEmbedded Image Tools.
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#

from mic import msger
from mic.utils import runner

def exec_cmd(cmd_and_args, as_shell = False, catch = 3):
    """
    Execute command, catching stderr, stdout

    Need to execute as_shell if the command uses wildcards
    """
    msger.debug("exec_cmd: %s" % cmd_and_args)
    args = cmd_and_args.split()
    msger.debug(args)

    if (as_shell):
        rc, out = runner.runtool(cmd_and_args, catch)
    else:
        rc, out = runner.runtool(args, catch)
    out = out.strip()
    msger.debug("exec_cmd: output for %s (rc = %d): %s" % \
                    (cmd_and_args, rc, out))

    if rc != 0:
        # We don't throw exception when return code is not 0, because
        # parted always fails to reload part table with loop devices. This
        # prevents us from distinguishing real errors based on return
        # code.
        msger.debug("WARNING: %s returned '%s' instead of 0" % (args[0], rc))

    return (rc, out)


def exec_cmd_quiet(cmd_and_args, as_shell = False):
    """
    Execute command, catching nothing in the output

    Need to execute as_shell if the command uses wildcards
    """
    return exec_cmd(cmd_and_args, as_shell, 0)


def exec_native_cmd(cmd_and_args, native_sysroot, catch = 3):
    """
    Execute native command, catching stderr, stdout

    Need to execute as_shell if the command uses wildcards

    Always need to execute native commands as_shell
    """
    native_paths = \
        "export PATH=%s/sbin:PATH=%s/usr/sbin:PATH=%s/usr/bin:$PATH" % \
        (native_sysroot, native_sysroot, native_sysroot)
    native_cmd_and_args = "%s;%s" % (native_paths, cmd_and_args)
    msger.debug("exec_native_cmd: %s" % cmd_and_args)

    args = cmd_and_args.split()
    msger.debug(args)

    return exec_cmd(native_cmd_and_args, True, catch)


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
