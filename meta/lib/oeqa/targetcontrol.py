# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# This module is used by testimage.bbclass for setting up and controlling a target machine.

import os
import shutil
import subprocess
import bb
import traceback
import sys
from oeqa.utils.sshcontrol import SSHControl
from oeqa.utils.qemurunner import QemuRunner
from oeqa.controllers.testtargetloader import TestTargetLoader
from abc import ABCMeta, abstractmethod

def get_target_controller(d):
    testtarget = d.getVar("TEST_TARGET", True)
    # old, simple names
    if testtarget == "qemu":
        return QemuTarget(d)
    elif testtarget == "simpleremote":
        return SimpleRemoteTarget(d)
    else:
        # use the class name
        try:
            # is it a core class defined here?
            controller = getattr(sys.modules[__name__], testtarget)
        except AttributeError:
            # nope, perhaps a layer defined one
            try:
                bbpath = d.getVar("BBPATH", True).split(':')
                testtargetloader = TestTargetLoader()
                controller = testtargetloader.get_controller_module(testtarget, bbpath)
            except ImportError as e:
                bb.fatal("Failed to import {0} from available controller modules:\n{1}".format(testtarget,traceback.format_exc()))
            except AttributeError as e:
                bb.fatal("Invalid TEST_TARGET - " + str(e))
        return controller(d)


class BaseTarget(object):

    __metaclass__ = ABCMeta

    def __init__(self, d):
        self.connection = None
        self.ip = None
        self.server_ip = None
        self.datetime = d.getVar('DATETIME', True)
        self.testdir = d.getVar("TEST_LOG_DIR", True)
        self.pn = d.getVar("PN", True)

    @abstractmethod
    def deploy(self):

        self.sshlog = os.path.join(self.testdir, "ssh_target_log.%s" % self.datetime)
        sshloglink = os.path.join(self.testdir, "ssh_target_log")
        if os.path.islink(sshloglink):
            os.unlink(sshloglink)
        os.symlink(self.sshlog, sshloglink)
        bb.note("SSH log file: %s" %  self.sshlog)

    @abstractmethod
    def start(self, params=None):
        pass

    @abstractmethod
    def stop(self):
        pass

    @abstractmethod
    def restart(self, params=None):
        pass

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
        addr = d.getVar("TEST_TARGET_IP", True) or bb.fatal('Please set TEST_TARGET_IP with the IP address of the machine you want to run the tests on.')
        self.ip = addr.split(":")[0]
        try:
            self.port = addr.split(":")[1]
        except IndexError:
            self.port = None
        bb.note("Target IP: %s" % self.ip)
        self.server_ip = d.getVar("TEST_SERVER_IP", True)
        if not self.server_ip:
            try:
                self.server_ip = subprocess.check_output(['ip', 'route', 'get', self.ip ]).split("\n")[0].split()[-1]
            except Exception as e:
                bb.fatal("Failed to determine the host IP address (alternatively you can set TEST_SERVER_IP with the IP address of this machine): %s" % e)
        bb.note("Server IP: %s" % self.server_ip)

    def deploy(self):
        super(SimpleRemoteTarget, self).deploy()

    def start(self, params=None):
        self.connection = SSHControl(self.ip, logfile=self.sshlog, port=self.port)

    def stop(self):
        self.connection = None
        self.ip = None
        self.server_ip = None

    def restart(self, params=None):
        pass
