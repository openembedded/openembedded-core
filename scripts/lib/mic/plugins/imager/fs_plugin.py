#!/usr/bin/python -tt
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

import os
import sys

from mic import chroot, msger
from mic.utils import cmdln, misc, errors, fs_related
from mic.imager import fs
from mic.conf import configmgr
from mic.plugin import pluginmgr

from mic.pluginbase import ImagerPlugin
class FsPlugin(ImagerPlugin):
    name = 'fs'

    @classmethod
    @cmdln.option("--include-src",
                  dest="include_src",
                  action="store_true",
                  default=False,
                  help="Generate a image with source rpms included")
    def do_create(self, subcmd, opts, *args):
        """${cmd_name}: create fs image

        Usage:
            ${name} ${cmd_name} <ksfile> [OPTS]

        ${cmd_option_list}
        """

        if len(args) != 1:
            raise errors.Usage("Extra arguments given")

        creatoropts = configmgr.create
        ksconf = args[0]

        if creatoropts['runtime'] == 'bootstrap':
            configmgr._ksconf = ksconf
            rt_util.bootstrap_mic()

        recording_pkgs = []
        if len(creatoropts['record_pkgs']) > 0:
            recording_pkgs = creatoropts['record_pkgs']

        if creatoropts['release'] is not None:
            if 'name' not in recording_pkgs:
                recording_pkgs.append('name')
            if 'vcs' not in recording_pkgs:
                recording_pkgs.append('vcs')

        configmgr._ksconf = ksconf

        # Called After setting the configmgr._ksconf as the creatoropts['name'] is reset there.
        if creatoropts['release'] is not None:
            creatoropts['outdir'] = "%s/%s/images/%s/" % (creatoropts['outdir'], creatoropts['release'], creatoropts['name'])

        # try to find the pkgmgr
        pkgmgr = None
        backends = pluginmgr.get_plugins('backend')
        if 'auto' == creatoropts['pkgmgr']:
            for key in configmgr.prefer_backends:
                if key in backends:
                    pkgmgr = backends[key]
                    break
        else:
            for key in backends.keys():
                if key == creatoropts['pkgmgr']:
                    pkgmgr = backends[key]
                    break

        if not pkgmgr:
            raise errors.CreatorError("Can't find backend: %s, "
                                      "available choices: %s" %
                                      (creatoropts['pkgmgr'],
                                       ','.join(backends.keys())))

        creator = fs.FsImageCreator(creatoropts, pkgmgr)
        creator._include_src = opts.include_src

        if len(recording_pkgs) > 0:
            creator._recording_pkgs = recording_pkgs

        self.check_image_exists(creator.destdir,
                                creator.pack_to,
                                [creator.name],
                                creatoropts['release'])

        try:
            creator.check_depend_tools()
            creator.mount(None, creatoropts["cachedir"])
            creator.install()
            #Download the source packages ###private options
            if opts.include_src:
                installed_pkgs =  creator.get_installed_packages()
                msger.info('--------------------------------------------------')
                msger.info('Generating the image with source rpms included ...')
                if not misc.SrcpkgsDownload(installed_pkgs, creatoropts["repomd"], creator._instroot, creatoropts["cachedir"]):
                    msger.warning("Source packages can't be downloaded")

            creator.configure(creatoropts["repomd"])
            creator.copy_kernel()
            creator.unmount()
            creator.package(creatoropts["outdir"])
            if creatoropts['release'] is not None:
                creator.release_output(ksconf, creatoropts['outdir'], creatoropts['release'])
            creator.print_outimage_info()
        except errors.CreatorError:
            raise
        finally:
            creator.cleanup()

        msger.info("Finished.")
        return 0

    @classmethod
    def do_chroot(self, target, cmd=[]):#chroot.py parse opts&args
        try:
            if len(cmd) != 0:
                cmdline = ' '.join(cmd)
            else:
                cmdline = "/bin/bash"
            envcmd = fs_related.find_binary_inchroot("env", target)
            if envcmd:
                cmdline = "%s HOME=/root %s" % (envcmd, cmdline)
            chroot.chroot(target, None, cmdline)
        finally:
            chroot.cleanup_after_chroot("dir", None, None, None)
            return 1
