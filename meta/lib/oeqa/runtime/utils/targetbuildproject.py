# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

from oeqa.utils.buildproject import BuildProject

class TargetBuildProject(BuildProject):

    def __init__(self, target, d, uri, foldername=None):
        self.target = target
        self.targetdir = "~/"
        BuildProject.__init__(self, d, uri, foldername, tmpdir="/tmp")

    def download_archive(self):

        self._download_archive()

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
    def _run(self, cmd):
        return self.target.run(cmd, 0)[0]


