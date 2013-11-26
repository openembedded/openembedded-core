# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# This module is used by testimage.bbclass for setting up and controlling a target machine.

import os
import shutil
import subprocess
import bb

from oeqa.utils.sshcontrol import SSHControl
from oeqa.utils.qemurunner import QemuRunner


def get_target_controller(d):
    if d.getVar("TEST_TARGET", True) == "qemu":
        return QemuTarget(d)
    elif d.getVar("TEST_TARGET", True) == "simpleremote":
        return SimpleRemoteTarget(d)
    else:
        bb.fatal("Please set a valid TEST_TARGET")


class BaseTarget(object):

    def __init__(self, d):
        self.connection = None
        self.ip = None
        self.server_ip = None
        self.datetime = d.getVar('DATETIME', True)
        self.testdir = d.getVar("TEST_LOG_DIR", True)
        self.pn = d.getVar("PN", True)

    def deploy(self):

        self.sshlog = os.path.join(self.testdir, "ssh_target_log.%s" % self.datetime)
        sshloglink = os.path.join(self.testdir, "ssh_target_log")
        if os.path.islink(sshloglink):
            os.unlink(sshloglink)
        os.symlink(self.sshlog, sshloglink)
        bb.note("SSH log file: %s" %  self.sshlog)

    def run(self, cmd, timeout=None):
        return self.connection.run(cmd, timeout)

    def copy_to(self, localpath, remotepath):
        return self.connection.copy_to(localpath, remotepath)

    def copy_from(self, remotepath, localpath):
        return self.connection.copy_from(remotepath, localpath)



class QemuTarget(BaseTarget):

    def __init__(self, d):

        super(QemuTarget, self).__init__(d)

        self.qemulog = os.path.join(self.testdir, "qemu_boot_log.%s" % self.datetime)
        self.origrootfs = os.path.join(d.getVar("DEPLOY_DIR_IMAGE", True),  d.getVar("IMAGE_LINK_NAME", True) + '.ext3')
        self.rootfs = os.path.join(self.testdir, d.getVar("IMAGE_LINK_NAME", True) + '-testimage.ext3')

        self.runner = QemuRunner(machine=d.getVar("MACHINE", True),
                        rootfs=self.rootfs,
                        tmpdir = d.getVar("TMPDIR", True),
                        deploy_dir_image = d.getVar("DEPLOY_DIR_IMAGE", True),
                        display = d.getVar("BB_ORIGENV", False).getVar("DISPLAY", True),
                        logfile = self.qemulog,
                        boottime = int(d.getVar("TEST_QEMUBOOT_TIMEOUT", True)))

    def deploy(self):
        try:
            shutil.copyfile(self.origrootfs, self.rootfs)
        except Exception as e:
            bb.fatal("Error copying rootfs: %s" % e)

        qemuloglink = os.path.join(self.testdir, "qemu_boot_log")
        if os.path.islink(qemuloglink):
            os.unlink(qemuloglink)
        os.symlink(self.qemulog, qemuloglink)

        bb.note("rootfs file: %s" %  self.rootfs)
        bb.note("Qemu log file: %s" % self.qemulog)
        super(QemuTarget, self).deploy()

    def start(self, params=None):
        if self.runner.start(params):
            self.ip = self.runner.ip
            self.server_ip = self.runner.server_ip
            self.connection = SSHControl(ip=self.ip, logfile=self.sshlog)
        else:
            raise bb.build.FuncFailed("%s - FAILED to start qemu - check the task log and the boot log" % self.pn)

    def stop(self):
        self.runner.stop()
        self.connection = None
        self.ip = None
        self.server_ip = None

    def restart(self, params=None):
        if self.runner.restart(params):
            self.ip = self.runner.ip
            self.server_ip = self.runner.server_ip
            self.connection = SSHControl(ip=self.ip, logfile=self.sshlog)
        else:
            raise bb.build.FuncFailed("%s - FAILED to re-start qemu - check the task log and the boot log" % self.pn)


class SimpleRemoteTarget(BaseTarget):

    def __init__(self, d):
        super(SimpleRemoteTarget, self).__init__(d)
        self.ip = d.getVar("TEST_TARGET_IP", True) or bb.fatal('Please set TEST_TARGET_IP with the IP address of the machine you want to run the tests on.')
        bb.note("Target IP: %s" % self.ip)
        self.server_ip = d.getVar("TEST_SERVER_IP", True)
        if not self.server_ip:
            try:
                self.server_ip = subprocess.check_output(['ip', 'route', 'get', self.ip ]).split()[6]
            except Exception as e:
                bb.fatal("Failed to determine the host IP address (alternatively you can set TEST_SERVER_IP with the IP address of this machine): %s" % e)
        bb.note("Server IP: %s" % self.server_ip)

    def deploy(self):
        super(SimpleRemoteTarget, self).deploy()

    def start(self, params=None):
        self.connection = SSHControl(self.ip, logfile=self.sshlog)

    def stop(self):
        self.connection = None
        self.ip = None
        self.server_ip = None

    def restart(self, params=None):
        pass
