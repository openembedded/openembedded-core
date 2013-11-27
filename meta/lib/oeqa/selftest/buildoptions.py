import unittest
import os
import logging
import re

from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake, get_bb_var
import oeqa.utils.ftools as ftools

class ImageOptionsTests(oeSelfTest):

    def test_incremental_image_generation(self):
        bitbake("-c cleanall core-image-minimal")
        self.write_config('INC_RPM_IMAGE_GEN = "1"')
        self.append_config('IMAGE_FEATURES += "ssh-server-openssh"')
        bitbake("core-image-minimal")
        res = runCmd("grep 'Installing openssh-sshd' %s" % (os.path.join(get_bb_var("WORKDIR", "core-image-minimal"), "temp/log.do_rootfs")), ignore_status=True)
        self.remove_config('IMAGE_FEATURES += "ssh-server-openssh"')
        self.assertEqual(0, res.status, msg="No match for openssh-sshd in log.do_rootfs")
        bitbake("core-image-minimal")
        res = runCmd("grep 'Removing openssh-sshd' %s" %(os.path.join(get_bb_var("WORKDIR", "core-image-minimal"), "temp/log.do_rootfs")),ignore_status=True)
        self.assertEqual(0, res.status, msg="openssh-sshd was not removed from image")

    def test_rm_old_image(self):
        bitbake("core-image-minimal")
        deploydir = get_bb_var("DEPLOY_DIR_IMAGE", target="core-image-minimal")
        imagename = get_bb_var("IMAGE_LINK_NAME", target="core-image-minimal")
        oldimgpath = os.path.realpath(os.path.join(deploydir, imagename + ".ext3"))
        self.append_config("RM_OLD_IMAGE = \"1\"")
        bitbake("core-image-minimal")
        self.assertFalse(os.path.exists(oldimgpath), msg="Old image path still exists: %s" % oldimgpath)

    def test_ccache_tool(self):
        bitbake("ccache-native")
        self.assertTrue(os.path.isfile(os.path.join(get_bb_var('STAGING_BINDIR_NATIVE', 'ccache-native'), "ccache")))
        self.write_config('INHERIT += "ccache"')
        bitbake("m4 -c cleansstate")
        bitbake("m4 -c compile")
        res = runCmd("grep ccache %s" % (os.path.join(get_bb_var("WORKDIR","m4"),"temp/log.do_compile")), ignore_status=True)
        self.assertEqual(0, res.status, msg="No match for ccache in m4 log.do_compile")
        bitbake("ccache-native -ccleansstate")


class DiskMonTest(oeSelfTest):

    def test_stoptask_behavior(self):
        result = runCmd("df -k %s" % os.getcwd())
        size = result.output.split("\n")[1].split()[3]
        self.append_config('BB_DISKMON_DIRS = "STOPTASKS,${TMPDIR},%sK,4510K"' % size)
        res = bitbake("core-image-minimal", ignore_status = True)
        self.assertTrue('ERROR: No new tasks can be executed since the disk space monitor action is "STOPTASKS"!' in res.output)
        self.assertEqual(res.status, 1)
        self.append_config('BB_DISKMON_DIRS = "ABORT,${TMPDIR},%sK,4510K"' % size)
        res = bitbake("core-image-minimal", ignore_status = True)
        self.assertTrue('ERROR: Immediately abort since the disk space monitor action is "ABORT"!' in res.output)
        self.assertEqual(res.status, 1)
        self.append_config('BB_DISKMON_DIRS = "WARN,${TMPDIR},%sK,4510K"' % size)
        res = bitbake("core-image-minimal")
        self.assertTrue('WARNING: The free space' in res.output)

class SanityOptionsTest(oeSelfTest):

    def test_options_warnqa_errorqa_switch(self):
        bitbake("xcursor-transparent-theme -ccleansstate")

        if "packages-list" not in get_bb_var("ERROR_QA"):
            self.write_config("ERROR_QA_append = \" packages-list\"")

        self.write_recipeinc('xcursor-transparent-theme', 'PACKAGES += \"${PN}-dbg\"')
        res = bitbake("xcursor-transparent-theme", ignore_status=True)
        self.delete_recipeinc('xcursor-transparent-theme')
        self.assertTrue("ERROR: QA Issue: xcursor-transparent-theme-dbg is listed in PACKAGES multiple times, this leads to packaging errors." in res.output)
        self.assertEqual(res.status, 1)
        self.write_recipeinc('xcursor-transparent-theme', 'PACKAGES += \"${PN}-dbg\"')
        self.append_config('ERROR_QA_remove = "packages-list"')
        self.append_config('WARN_QA_append = " packages-list"')
        bitbake("xcursor-transparent-theme")
        bitbake("xcursor-transparent-theme -ccleansstate")
        self.delete_recipeinc('xcursor-transparent-theme')

    def test_sanity_userspace_dependency(self):
        self.append_config('WARN_QA_append = " unsafe-references-in-binaries unsafe-references-in-scripts"')
        bitbake("-ccleansstate gzip nfs-utils")
        res = bitbake("gzip nfs-utils")
        self.assertTrue("WARNING: QA Issue: gzip" in res.output)
        self.assertTrue("WARNING: QA Issue: nfs-utils" in res.output)
