# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os
import subprocess

from oeqa.utils.buildproject import BuildProject

class SDKBuildProject(BuildProject):

    def __init__(self, testpath, sdkenv, d, uri, foldername=None):
        self.sdkenv = sdkenv
        self.testdir = testpath
        self.targetdir = testpath
        bb.utils.mkdirhier(testpath)
        self.datetime = d.getVar('DATETIME')
        self.testlogdir = d.getVar("TEST_LOG_DIR")
        bb.utils.mkdirhier(self.testlogdir)
        self.logfile = os.path.join(self.testlogdir, "sdk_target_log.%s" % self.datetime)
        BuildProject.__init__(self, d, uri, foldername, tmpdir=testpath)

    def download_archive(self):

        self._download_archive()

        cmd = 'tar xf %s%s -C %s' % (self.targetdir, self.archive, self.targetdir)
        subprocess.check_call(cmd, shell=True)

        #Change targetdir to project folder
        self.targetdir = os.path.join(self.targetdir, self.fname)

    def run_configure(self, configure_args='', extra_cmds=' gnu-configize; '):
        return super(SDKBuildProject, self).run_configure(configure_args=(configure_args or '$CONFIGURE_FLAGS'), extra_cmds=extra_cmds)

    def run_install(self, install_args=''):
        return super(SDKBuildProject, self).run_install(install_args=(install_args or "DESTDIR=%s/../install" % self.targetdir))

    def log(self, msg):
        if self.logfile:
            with open(self.logfile, "a") as f:
               f.write("%s\n" % msg)

    def _run(self, cmd):
        self.log("Running . %s; " % self.sdkenv + cmd)
        return subprocess.call(". %s; " % self.sdkenv + cmd, shell=True)
