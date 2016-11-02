# Copyright (C) 2013-2016 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Provides a class for automating build tests for projects

import os
import re
import bb.utils
import subprocess
from abc import ABCMeta, abstractmethod

class BuildProject(metaclass=ABCMeta):

    def __init__(self, d, uri, foldername=None, tmpdir="/tmp/"):
        self.d = d
        self.uri = uri
        self.archive = os.path.basename(uri)
        self.localarchive = os.path.join(tmpdir,self.archive)
        if foldername:
            self.fname = foldername
        else:
            self.fname = re.sub(r'\.tar\.bz2$|\.tar\.gz$|\.tar\.xz$', '', self.archive)

    # Download self.archive to self.localarchive
    def _download_archive(self):

        dl_dir = self.d.getVar("DL_DIR")
        if dl_dir and os.path.exists(os.path.join(dl_dir, self.archive)):
            bb.utils.copyfile(os.path.join(dl_dir, self.archive), self.localarchive)
            return

        exportvars = ['HTTP_PROXY', 'http_proxy',
                      'HTTPS_PROXY', 'https_proxy',
                      'FTP_PROXY', 'ftp_proxy',
                      'FTPS_PROXY', 'ftps_proxy',
                      'NO_PROXY', 'no_proxy',
                      'ALL_PROXY', 'all_proxy',
                      'SOCKS5_USER', 'SOCKS5_PASSWD']

        cmd = ''
        for var in exportvars:
            val = self.d.getVar(var)
            if val:
                cmd = 'export ' + var + '=\"%s\"; %s' % (val, cmd)

        cmd = cmd + "wget -O %s %s" % (self.localarchive, self.uri)
        subprocess.check_call(cmd, shell=True)

    # This method should provide a way to run a command in the desired environment.
    @abstractmethod
    def _run(self, cmd):
        pass

    # The timeout parameter of target.run is set to 0 to make the ssh command
    # run with no timeout.
    def run_configure(self, configure_args='', extra_cmds=''):
        return self._run('cd %s; %s ./configure %s' % (self.targetdir, extra_cmds, configure_args))

    def run_make(self, make_args=''):
        return self._run('cd %s; make %s' % (self.targetdir, make_args))

    def run_install(self, install_args=''):
        return self._run('cd %s; make install %s' % (self.targetdir, install_args))

    def clean(self):
        self._run('rm -rf %s' % self.targetdir)
        subprocess.call('rm -f %s' % self.localarchive, shell=True)
        pass
