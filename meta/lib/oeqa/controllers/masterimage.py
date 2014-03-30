import os
import bb
import traceback
import time

import oeqa.targetcontrol
import oeqa.utils.sshcontrol as sshcontrol
import oeqa.utils.commands as commands

class GummibootTarget(oeqa.targetcontrol.SimpleRemoteTarget):

    def __init__(self, d):
        # let our base class do the ip thing
        super(GummibootTarget, self).__init__(d)

        # test rootfs + kernel
        self.rootfs = os.path.join(d.getVar("DEPLOY_DIR_IMAGE", True), d.getVar("IMAGE_LINK_NAME", True) + '.tar.gz')
        self.kernel = os.path.join(d.getVar("DEPLOY_DIR_IMAGE", True), d.getVar("KERNEL_IMAGETYPE"))
        if not os.path.isfile(self.rootfs):
            # we could've checked that IMAGE_FSTYPES contains tar.gz but the config for running testimage might not be
            # the same as the config with which the image was build, ie
            # you bitbake core-image-sato with IMAGE_FSTYPES += "tar.gz"
            # and your autobuilder overwrites the config, adds the test bits and runs bitbake core-image-sato -c testimage
            bb.fatal("No rootfs found. Did you build the image ?\nIf yes, did you build it with IMAGE_FSTYPES += \"tar.gz\" ? \
                      \nExpected path: %s" % self.rootfs)
        if not os.path.isfile(self.kernel):
            bb.fatal("No kernel found. Expected path: %s" % self.kernel)

        # if the user knows what he's doing, then by all means...
        # test-rootfs.tar.gz and test-kernel are hardcoded names in other places
        # they really have to be used like that in commands though
        cmds = d.getVar("TEST_DEPLOY_CMDS", True)

        # this the value we need to set in the LoaderEntryOneShot EFI variable
        # so the system boots the 'test' bootloader label and not the default
        # The first four bytes are EFI bits, and the rest is an utf-16le string
        # (EFI vars values need to be utf-16)
        # $ echo -en "test\0" | iconv -f ascii -t utf-16le | hexdump -C
        # 00000000  74 00 65 00 73 00 74 00  00 00                    |t.e.s.t...|
        self.efivarvalue = r'\x07\x00\x00\x00\x74\x00\x65\x00\x73\x00\x74\x00\x00\x00'

        if cmds:
            self.deploy_cmds = cmds.split("\n")
        else:
            self.deploy_cmds = [
                'mount -L boot /boot',
                'mkdir -p /mnt/testrootfs',
                'mount -L testrootfs /mnt/testrootfs',
                'modprobe efivarfs',
                'mount -t efivarfs efivarfs /sys/firmware/efi/efivars',
                'cp ~/test-kernel /boot',
                'rm -rf /mnt/testrootfs/*',
                'tar xzvf ~/test-rootfs.tar.gz -C /mnt/testrootfs',
                'printf "%s" > /sys/firmware/efi/efivars/LoaderEntryOneShot-4a67b082-0a4c-41cf-b6c7-440b29bb8c4f' % self.efivarvalue
                ]

        # master ssh connection
        self.master = None

        # this is the name of the command that controls the power for a board
        # e.g: TEST_POWERCONTROL_CMD = "/home/user/myscripts/powercontrol.py ${MACHINE} what-ever-other-args-the-script-wants"
        # the command should take as the last argument "off" and "on" and "cycle" (off, on)
        self.powercontrol_cmd = d.getVar("TEST_POWERCONTROL_CMD", True) or None
        self.powercontrol_args = d.getVar("TEST_POWERCONTROL_EXTRA_ARGS") or ""
        self.origenv = os.environ
        if self.powercontrol_cmd:
            if self.powercontrol_args:
                self.powercontrol_cmd = "%s %s" % (self.powercontrol_cmd, self.powercontrol_args)
            # the external script for controlling power might use ssh
            # ssh + keys means we need the original user env
            bborigenv = d.getVar("BB_ORIGENV", False) or {}
            for key in bborigenv:
                val = bborigenv.getVar(key, True)
                if val is not None:
                    self.origenv[key] = str(val)
            self.power_ctl("on")

    def power_ctl(self, msg):
        if self.powercontrol_cmd:
            cmd = "%s %s" % (self.powercontrol_cmd, msg)
            commands.runCmd(cmd, preexec_fn=os.setsid, env=self.origenv)

    def power_cycle(self, conn):
        if self.powercontrol_cmd:
            # be nice, don't just cut power
            conn.run("shutdown -h now")
            time.sleep(10)
            self.power_ctl("cycle")
        else:
            status, output = conn.run("reboot")
            if status != 0:
                bb.error("Failed rebooting target and no power control command defined. You need to manually reset the device.\n%s" % output)

    def deploy(self):
        bb.plain("%s - deploying image on target" % self.pn)
        # base class just sets the ssh log file for us
        super(GummibootTarget, self).deploy()
        self.master = sshcontrol.SSHControl(ip=self.ip, logfile=self.sshlog, timeout=600, port=self.port)
        try:
            self._deploy()
        except Exception as e:
            bb.fatal("Failed deploying test image: %s" % e)

    def _deploy(self):
        # make sure we are in the right image
        status, output = self.master.run("cat /etc/masterimage")
        if status != 0:
            raise Exception("No ssh connectivity or target isn't running a master image.\n%s" % output)

        # make sure these aren't mounted
        self.master.run("umount /boot; umount /mnt/testrootfs; umount /sys/firmware/efi/efivars;")

        # from now on, every deploy cmd should return 0
        # else an exception will be thrown by sshcontrol
        self.master.ignore_status = False
        self.master.copy_to(self.rootfs, "~/test-rootfs.tar.gz")
        self.master.copy_to(self.kernel, "~/test-kernel")
        for cmd in self.deploy_cmds:
            self.master.run(cmd)


    def start(self, params=None):
        bb.plain("%s - boot test image on target" % self.pn)
        self.power_cycle(self.master)
        # there are better ways than a timeout but this should work for now
        time.sleep(120)
        # set the ssh object for the target/test image
        self.connection = sshcontrol.SSHControl(self.ip, logfile=self.sshlog, port=self.port)
        bb.plain("%s - start running tests" % self.pn)

    def stop(self):
        bb.plain("%s - reboot/powercycle target" % self.pn)
        self.power_cycle(self.connection)
