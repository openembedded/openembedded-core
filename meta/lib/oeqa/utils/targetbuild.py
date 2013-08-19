# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Provides a class for automating build tests for projects

from oeqa.oetest import oeRuntimeTest
import bb.fetch2
import bb.data
import os
import re


class TargetBuildProject():

    def __init__(self, target, uri, foldername=None):
        self.target = target
        self.uri = uri
        self.targetdir = "/home/root/"

        self.localdata = bb.data.createCopy(oeRuntimeTest.tc.d)
        bb.data.update_data(self.localdata)

        if not foldername:
            self.archive = os.path.basename(uri)
            self.fname = re.sub(r'.tar.bz2|tar.gz$', '', self.archive)
        else:
            self.fname = foldername

    def download_archive(self):

        try:
            self.localdata.delVar("BB_STRICT_CHECKSUM")
            fetcher = bb.fetch2.Fetch([self.uri], self.localdata)
            fetcher.download()
            self.localarchive = fetcher.localpath(self.uri)
        except bb.fetch2.BBFetchException:
            raise Exception("Failed to download archive: %s" % self.uri)

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
