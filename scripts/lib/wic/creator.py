#!/usr/bin/env python -tt
#
# Copyright (c) 2011 Intel, Inc.
#
# This program is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the Free
# Software Foundation; version 2 of the License
#
# This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc., 59
# Temple Place - Suite 330, Boston, MA 02111-1307, USA.

import os, sys, re
from optparse import SUPPRESS_HELP

from wic import msger
from wic.utils import cmdln, errors
from wic.conf import configmgr
from wic.plugin import pluginmgr


class Creator(cmdln.Cmdln):
    """${name}: create an image

    Usage:
        ${name} SUBCOMMAND <ksfile> [OPTS]

    ${command_list}
    ${option_list}
    """

    name = 'wic create(cr)'

    def __init__(self, *args, **kwargs):
        cmdln.Cmdln.__init__(self, *args, **kwargs)
        self._subcmds = []

        # get cmds from pluginmgr
        # mix-in do_subcmd interface
        for subcmd, klass in pluginmgr.get_plugins('imager').iteritems():
            if not hasattr(klass, 'do_create'):
                msger.warning("Unsupported subcmd: %s" % subcmd)
                continue

            func = getattr(klass, 'do_create')
            setattr(self.__class__, "do_"+subcmd, func)
            self._subcmds.append(subcmd)

    def get_optparser(self):
        optparser = cmdln.CmdlnOptionParser(self)
        optparser.add_option('-d', '--debug', action='store_true',
                             dest='debug',
                             help=SUPPRESS_HELP)
        optparser.add_option('-v', '--verbose', action='store_true',
                             dest='verbose',
                             help=SUPPRESS_HELP)
        optparser.add_option('', '--logfile', type='string', dest='logfile',
                             default=None,
                             help='Path of logfile')
        optparser.add_option('-c', '--config', type='string', dest='config',
                             default=None,
                             help='Specify config file for wic')
        optparser.add_option('-o', '--outdir', type='string', action='store',
                             dest='outdir', default=None,
                             help='Output directory')
        optparser.add_option('', '--tmpfs', action='store_true', dest='enabletmpfs',
                             help='Setup tmpdir as tmpfs to accelerate, experimental'
                                  ' feature, use it if you have more than 4G memory')
        return optparser

    def preoptparse(self, argv):
        optparser = self.get_optparser()

        largs = []
        rargs = []
        while argv:
            arg = argv.pop(0)

            if arg in ('-h', '--help'):
                rargs.append(arg)

            elif optparser.has_option(arg):
                largs.append(arg)

                if optparser.get_option(arg).takes_value():
                    try:
                        largs.append(argv.pop(0))
                    except IndexError:
                        raise errors.Usage("option %s requires arguments" % arg)

            else:
                if arg.startswith("--"):
                    if "=" in arg:
                        opt = arg.split("=")[0]
                    else:
                        opt = None
                elif arg.startswith("-") and len(arg) > 2:
                    opt = arg[0:2]
                else:
                    opt = None

                if opt and optparser.has_option(opt):
                    largs.append(arg)
                else:
                    rargs.append(arg)

        return largs + rargs

    def postoptparse(self):
        abspath = lambda pth: os.path.abspath(os.path.expanduser(pth))

        if self.options.verbose:
            msger.set_loglevel('verbose')
        if self.options.debug:
            msger.set_loglevel('debug')

        if self.options.logfile:
            logfile_abs_path = abspath(self.options.logfile)
            if os.path.isdir(logfile_abs_path):
                raise errors.Usage("logfile's path %s should be file"
                                   % self.options.logfile)
            if not os.path.exists(os.path.dirname(logfile_abs_path)):
                os.makedirs(os.path.dirname(logfile_abs_path))
            msger.set_interactive(False)
            msger.set_logfile(logfile_abs_path)
            configmgr.create['logfile'] = self.options.logfile

        if self.options.config:
            configmgr.reset()
            configmgr._siteconf = self.options.config

        if self.options.outdir is not None:
            configmgr.create['outdir'] = abspath(self.options.outdir)

        cdir = 'outdir'
        if os.path.exists(configmgr.create[cdir]) \
           and not os.path.isdir(configmgr.create[cdir]):
            msger.error('Invalid directory specified: %s' \
                        % configmgr.create[cdir])

        if self.options.enabletmpfs:
            configmgr.create['enabletmpfs'] = self.options.enabletmpfs

    def main(self, argv=None):
        if argv is None:
            argv = sys.argv
        else:
            argv = argv[:] # don't modify caller's list

        self.optparser = self.get_optparser()
        if self.optparser:
            try:
                argv = self.preoptparse(argv)
                self.options, args = self.optparser.parse_args(argv)

            except cmdln.CmdlnUserError, ex:
                msg = "%s: %s\nTry '%s help' for info.\n"\
                      % (self.name, ex, self.name)
                msger.error(msg)

            except cmdln.StopOptionProcessing, ex:
                return 0
        else:
            # optparser=None means no process for opts
            self.options, args = None, argv[1:]

        if not args:
            return self.emptyline()

        self.postoptparse()

        return self.cmd(args)

    def precmd(self, argv): # check help before cmd

        if '-h' in argv or '?' in argv or '--help' in argv or 'help' in argv:
            return argv

        if len(argv) == 1:
            return ['help', argv[0]]

        return argv
