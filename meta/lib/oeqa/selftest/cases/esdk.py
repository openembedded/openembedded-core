#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import tempfile
import shutil
import os
import glob
import time
from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import runCmd, bitbake, get_bb_vars

class oeSDKExtSelfTest(OESelftestTestCase):
    """
    # Bugzilla Test Plan: 6033
    # This code is planned to be part of the automation for eSDK containig
    # Install libraries and headers, image generation binary feeds, sdk-update.
    """

    @staticmethod
    def get_esdk_environment(env_eSDK, tmpdir_eSDKQA):
        # XXX: at this time use the first env need to investigate
        # what environment load oe-selftest, i586, x86_64
        pattern = os.path.join(tmpdir_eSDKQA, 'environment-setup-*')
        return glob.glob(pattern)[0]

    @staticmethod
    def run_esdk_cmd(env_eSDK, tmpdir_eSDKQA, cmd, **options):
        if not options:
            options = {}
        if not 'shell' in options:
            options['shell'] = True

        return runCmd("cd %s; unset BBPATH; unset BUILDDIR; . %s; %s" % (tmpdir_eSDKQA, env_eSDK, cmd), **options)

    @staticmethod
    def generate_eSDK(image):
        pn_task = '%s -c populate_sdk_ext' % image
        bitbake(pn_task)

    @staticmethod
    def get_eSDK_toolchain(image):
        pn_task = '%s -c populate_sdk_ext' % image

        bb_vars = get_bb_vars(['SDK_DEPLOY', 'TOOLCHAINEXT_OUTPUTNAME'], pn_task)
        sdk_deploy = bb_vars['SDK_DEPLOY']
        toolchain_name = bb_vars['TOOLCHAINEXT_OUTPUTNAME']
        return os.path.join(sdk_deploy, toolchain_name + '.sh')

    @staticmethod
    def update_configuration(cls, image, tmpdir_eSDKQA, env_eSDK, ext_sdk_path):
        sstate_dir = os.path.join(os.environ['BUILDDIR'], 'sstate-cache')

        oeSDKExtSelfTest.generate_eSDK(cls.image)

        cls.ext_sdk_path = oeSDKExtSelfTest.get_eSDK_toolchain(cls.image)
        runCmd("%s -y -d \"%s\"" % (cls.ext_sdk_path, cls.tmpdir_eSDKQA))

        cls.env_eSDK = oeSDKExtSelfTest.get_esdk_environment('', cls.tmpdir_eSDKQA)

        sstate_config="""
ESDK_LOCALCONF_ALLOW = "SSTATE_MIRRORS"
SSTATE_MIRRORS =  "file://.* file://%s/PATH"
CORE_IMAGE_EXTRA_INSTALL = "perl"
        """ % sstate_dir

        with open(os.path.join(cls.tmpdir_eSDKQA, 'conf', 'local.conf'), 'a+') as f:
            f.write(sstate_config)

    @classmethod
    def setUpClass(cls):
        super(oeSDKExtSelfTest, cls).setUpClass()
        cls.image = 'core-image-minimal'

        bb_vars = get_bb_vars(['SSTATE_DIR', 'WORKDIR'], cls.image)
        bb.utils.mkdirhier(bb_vars["WORKDIR"])
        cls.tmpdirobj = tempfile.TemporaryDirectory(prefix="selftest-esdk-", dir=bb_vars["WORKDIR"])
        cls.tmpdir_eSDKQA = cls.tmpdirobj.name

        oeSDKExtSelfTest.generate_eSDK(cls.image)

        # Install eSDK
        cls.ext_sdk_path = oeSDKExtSelfTest.get_eSDK_toolchain(cls.image)
        runCmd("%s -y -d \"%s\"" % (cls.ext_sdk_path, cls.tmpdir_eSDKQA))

        cls.env_eSDK = oeSDKExtSelfTest.get_esdk_environment('', cls.tmpdir_eSDKQA)

        # Configure eSDK to use sstate mirror from poky
        sstate_config="""
ESDK_LOCALCONF_ALLOW = "SSTATE_MIRRORS"
SSTATE_MIRRORS =  "file://.* file://%s/PATH"
            """ % bb_vars["SSTATE_DIR"]
        with open(os.path.join(cls.tmpdir_eSDKQA, 'conf', 'local.conf'), 'a+') as f:
            f.write(sstate_config)

    @classmethod
    def tearDownClass(cls):
        for i in range(0, 10):
            if os.path.exists(os.path.join(cls.tmpdir_eSDKQA, 'bitbake.lock')) or os.path.exists(os.path.join(cls.tmpdir_eSDKQA, 'cache/hashserv.db-wal')):
                time.sleep(1)
            else:
                break
        cls.tmpdirobj.cleanup()
        super().tearDownClass()

    def test_install_libraries_headers(self):
        pn_sstate = 'bc'
        bitbake(pn_sstate)
        cmd = "devtool sdk-install %s " % pn_sstate
        oeSDKExtSelfTest.run_esdk_cmd(self.env_eSDK, self.tmpdir_eSDKQA, cmd)

    def test_sdk_install_assembles_the_shared_sysroot(self):
        """
        A recipe's commands are only on PATH once the shared sysroot has
        been assembled from the components directory, and sdk-install
        assembles it only when it had something to install. Install a
        recipe, discard the assembled sysroot, and ask for the recipe
        again: it is already staged, so there is nothing to install, and
        the commands have to be reachable regardless.

        The recipe has to be a native one. An eSDK builds PATH from the
        native sysroot alone, so a target recipe's commands are not
        reachable however the sysroot was assembled.
        """
        recipe = 'bc-native'
        command = 'bc'
        bitbake(recipe)
        install = "devtool sdk-install %s" % recipe
        oeSDKExtSelfTest.run_esdk_cmd(self.env_eSDK, self.tmpdir_eSDKQA, install)

        # the state devtool build leaves behind: staged, not assembled
        oeSDKExtSelfTest.run_esdk_cmd(self.env_eSDK, self.tmpdir_eSDKQA,
                                      "bitbake build-sysroots -c clean")
        oeSDKExtSelfTest.run_esdk_cmd(self.env_eSDK, self.tmpdir_eSDKQA, install)

        # The eSDK environment appends the caller's PATH, and bc is not a
        # host tool oe requires, so an unassembled sysroot resolves either
        # to the build machine's copy or to nothing at all. Neither is a
        # command error, so ask without raising and report which happened.
        result = oeSDKExtSelfTest.run_esdk_cmd(self.env_eSDK, self.tmpdir_eSDKQA,
                                               "command -v %s" % command,
                                               ignore_status=True)
        # sourcing the eSDK environment prints a banner, and run_esdk_cmd
        # merges stderr into the output, so the answer is the last line
        found = ""
        if result.status == 0:
            found = result.output.strip().splitlines()[-1].strip()
        self.assertTrue(found.startswith(self.tmpdir_eSDKQA),
                        "sdk-install left %s out of the eSDK's shared sysroot; "
                        "%s resolved to %s"
                        % (command, command, found or "nothing"))

    def test_image_generation_binary_feeds(self):
        image = 'core-image-minimal'
        cmd = "devtool build-image %s" % image
        oeSDKExtSelfTest.run_esdk_cmd(self.env_eSDK, self.tmpdir_eSDKQA, cmd)
