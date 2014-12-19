#!/usr/bin/env python

# Development tool - utility functions for plugins
#
# Copyright (C) 2014 Intel Corporation
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
import sys
import subprocess
import logging

logger = logging.getLogger('devtool')

def exec_build_env_command(init_path, builddir, cmd, watch=False, **options):
    import bb
    if not 'cwd' in options:
        options["cwd"] = builddir
    if init_path:
        logger.debug('Executing command: "%s" using init path %s' % (cmd, init_path))
        init_prefix = '. %s %s > /dev/null && ' % (init_path, builddir)
    else:
        logger.debug('Executing command "%s"' % cmd)
        init_prefix = ''
    if watch:
        if sys.stdout.isatty():
            # Fool bitbake into thinking it's outputting to a terminal (because it is, indirectly)
            cmd = 'script -q -c "%s" /dev/null' % cmd
        return exec_watch('%s%s' % (init_prefix, cmd), **options)
    else:
        return bb.process.run('%s%s' % (init_prefix, cmd), **options)

def exec_watch(cmd, **options):
    if isinstance(cmd, basestring) and not "shell" in options:
        options["shell"] = True

    process = subprocess.Popen(
        cmd, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, **options
    )

    buf = ''
    while True:
        out = process.stdout.read(1)
        if out:
            sys.stdout.write(out)
            sys.stdout.flush()
            buf += out
        elif out == '' and process.poll() != None:
            break
    return buf

def setup_tinfoil():
    import scriptpath
    bitbakepath = scriptpath.add_bitbake_lib_path()
    if not bitbakepath:
        logger.error("Unable to find bitbake by searching parent directory of this script or PATH")
        sys.exit(1)

    import bb.tinfoil
    import logging
    tinfoil = bb.tinfoil.Tinfoil()
    tinfoil.prepare(False)
    tinfoil.logger.setLevel(logging.WARNING)
    return tinfoil

