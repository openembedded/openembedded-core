# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Provides a class for automating build tests for projects

import os
import re
import subprocess


class TargetBuildProject():

    def __init__(self, target, d, uri, foldername=None):
        self.target = target
        self.d = d
        self.uri = uri
        self.targetdir = "~/"
        self.archive = os.path.basename(uri)
        self.localarchive = "/tmp/" + self.archive
        self.fname = re.sub(r'.tar.bz2|tar.gz$', '', self.archive)
        if foldername:
            self.fname = foldername

    def download_archive(self):

        exportvars = ['HTTP_PROXY', 'http_proxy',
                      'HTTPS_PROXY', 'https_proxy',
                      'FTP_PROXY', 'ftp_proxy',
                      'FTPS_PROXY', 'ftps_proxy',
                      'NO_PROXY', 'no_proxy',
                      'ALL_PROXY', 'all_proxy',
                      'SOCKS5_USER', 'SOCKS5_PASSWD']

        cmd = ''
        for var in exportvars:
            val = self.d.getVar(var, True)
            if val:
                cmd = 'export ' + var + '=\"%s\"; %s' % (val, cmd)

        cmd = cmd + "wget -O %s %s" % (self.localarchive, self.uri)
        subprocess.check_call(cmd, shell=True)

        (status, output) = self.target.copy_to(self.localarchive, self.targetdir)
        if status != 0:
            raise Exception("Failed to copy archive to target, output: %s" % output)

        (status, output) = self.target.run('tar xf %s%s -C %s' % (self.targetdir, self.archive, self.targetdir))
        if status != 0:
            raise Exception("Failed to extract archive, output: %s" % output)

        #Change targetdir to project folder
        self.targetdir = self.targetdir + self.fname

    # The timeout parameter of target.run is set to 0 to make the ssh command
    # run with no timeout.
    def run_configure(self):
        return self.target.run('cd %s; ./configure' % self.targetdir, 0)[0]

    def run_make(self):
        return self.target.run('cd %s; make' % self.targetdir, 0)[0]

    def run_install(self):
        return self.target.run('cd %s; make install' % self.targetdir, 0)[0]

    def clean(self):
        self.target.run('rm -rf %s' % self.targetdir)
        subprocess.call('rm -f %s' % self.localarchive, shell=True)
