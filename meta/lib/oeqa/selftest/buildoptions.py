import unittest
import os
import logging
import re

from oeqa.selftest.base import oeSelfTest
from oeqa.selftest.buildhistory import BuildhistoryBase
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
        deploydir_files = os.listdir(deploydir)
        track_original_files = []
        for image_file in deploydir_files:
            if imagename in image_file and os.path.islink(os.path.join(deploydir, image_file)):
                track_original_files.append(os.path.realpath(os.path.join(deploydir, image_file)))
        self.append_config("RM_OLD_IMAGE = \"1\"")
        bitbake("-C rootfs core-image-minimal")
        deploydir_files = os.listdir(deploydir)
        remaining_not_expected = [path for path in track_original_files if os.path.basename(path) in deploydir_files]
        self.assertFalse(remaining_not_expected, msg="\nThe following image files ware not removed: %s" % ', '.join(map(str, remaining_not_expected)))

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
        result = runCmd("df -Pk %s" % os.getcwd())
        size = result.output.split("\n")[1].split()[3]
        self.write_config('BB_DISKMON_DIRS = "STOPTASKS,${TMPDIR},%sK,4510K"' % size)
        res = bitbake("m4", ignore_status = True)
        self.assertTrue('ERROR: No new tasks can be executed since the disk space monitor action is "STOPTASKS"!' in res.output)
        self.assertEqual(res.status, 1)
        self.write_config('BB_DISKMON_DIRS = "ABORT,${TMPDIR},%sK,4510K"' % size)
        res = bitbake("m4", ignore_status = True)
        self.assertTrue('ERROR: Immediately abort since the disk space monitor action is "ABORT"!' in res.output)
        self.assertEqual(res.status, 1)
        self.write_config('BB_DISKMON_DIRS = "WARN,${TMPDIR},%sK,4510K"' % size)
        res = bitbake("m4")
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
        res = bitbake("xcursor-transparent-theme")
        bitbake("xcursor-transparent-theme -ccleansstate")
        self.delete_recipeinc('xcursor-transparent-theme')
        self.assertTrue("WARNING: QA Issue: xcursor-transparent-theme-dbg is listed in PACKAGES multiple times, this leads to packaging errors." in res.output)

    def test_sanity_userspace_dependency(self):
        self.append_config('WARN_QA_append = " unsafe-references-in-binaries unsafe-references-in-scripts"')
        bitbake("-ccleansstate gzip nfs-utils")
        res = bitbake("gzip nfs-utils")
        self.assertTrue("WARNING: QA Issue: gzip" in res.output)
        self.assertTrue("WARNING: QA Issue: nfs-utils" in res.output)

class BuildhistoryTests(BuildhistoryBase):

    def test_buildhistory_basic(self):
        self.run_buildhistory_operation('xcursor-transparent-theme')
        self.assertTrue(os.path.isdir(get_bb_var('BUILDHISTORY_DIR')))

    def test_buildhistory_buildtime_pr_backwards(self):
        self.add_command_to_tearDown('cleanup-workdir')
        target = 'xcursor-transparent-theme'
        error = "ERROR: QA Issue: Package version for package %s went backwards which would break package feeds from (.*-r1 to .*-r0)" % target
        self.run_buildhistory_operation(target, target_config="PR = \"r1\"", change_bh_location=True)
        self.run_buildhistory_operation(target, target_config="PR = \"r0\"", change_bh_location=False, expect_error=True, error_regex=error)






