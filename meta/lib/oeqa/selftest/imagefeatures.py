from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake, get_bb_var, runqemu
from oeqa.utils.decorators import testcase
from oeqa.utils.sshcontrol import SSHControl
from os.path import isfile
from os import system
import glob
import os
import sys
import logging

class ImageFeatures(oeSelfTest):

    test_user = 'tester'
    root_user = 'root'

    @testcase(1107)
    def test_non_root_user_can_connect_via_ssh_without_password(self):
        """
        Summary: Check if non root user can connect via ssh without password
        Expected: 1. Connection to the image via ssh using root user without providing a password should be allowed.
                  2. Connection to the image via ssh using tester user without providing a password should be allowed.
        Product: oe-core
        Author: Ionut Chisanovici <ionutx.chisanovici@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """

        features = 'EXTRA_IMAGE_FEATURES += "ssh-server-openssh empty-root-password"\n'
        features += 'INHERIT += "extrausers"\n'
        features += 'EXTRA_USERS_PARAMS = "useradd -p \'\' {}; usermod -s /bin/sh {};"'.format(self.test_user, self.test_user)

        # Append 'features' to local.conf
        self.append_config(features)

        # Build a core-image-minimal
        bitbake('core-image-minimal')

        with runqemu("core-image-minimal", self) as qemu:
            # Attempt to ssh with each user into qemu with empty password
            for user in [self.root_user, self.test_user]:
                ssh = SSHControl(ip=qemu.ip, logfile=qemu.sshlog, user=user)
                status, output = ssh.run("true")
                self.assertEqual(status, 0, 'ssh to user %s failed with %s' % (user, output))

    @testcase(1115)
    def test_all_users_can_connect_via_ssh_without_password(self):
        """
        Summary:     Check if all users can connect via ssh without password
        Expected:    1. Connection to the image via ssh using root or tester user without providing a password should be allowed.
        Product:     oe-core
        Author:      Ionut Chisanovici <ionutx.chisanovici@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """
        features = 'EXTRA_IMAGE_FEATURES += "ssh-server-openssh allow-empty-password"\n'
        features += 'INHERIT += "extrausers"\n'
        features += 'EXTRA_USERS_PARAMS = "useradd -p \'\' {}; usermod -s /bin/sh {};"'.format(self.test_user, self.test_user)

        # Append 'features' to local.conf
        self.append_config(features)

        # Build a core-image-minimal
        bitbake('core-image-minimal')

        with runqemu("core-image-minimal", self) as qemu:
            # Attempt to ssh with each user into qemu with empty password
            for user in [self.root_user, self.test_user]:
                ssh = SSHControl(ip=qemu.ip, logfile=qemu.sshlog, user=user)
                status, output = ssh.run("true")
                self.assertEqual(status, 0, 'ssh to user tester failed with %s' % output)


    @testcase(1114)
    def test_rpm_version_4_support_on_image(self):
        """
        Summary:     Check rpm version 4 support on image
        Expected:    Rpm version must be 4.11.2
        Product:     oe-core
        Author:      Ionut Chisanovici <ionutx.chisanovici@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """

        rpm_version = '4.11.2'
        features = 'IMAGE_INSTALL_append = " rpm"\n'
        features += 'PREFERRED_VERSION_rpm = "{}"\n'.format(rpm_version)
        features += 'PREFERRED_VERSION_rpm-native = "{}"\n'.format(rpm_version)
        features += 'RPMROOTFSDEPENDS_remove = "rpmresolve-native:do_populate_sysroot"'

        # Append 'features' to local.conf
        self.append_config(features)

        # Build a core-image-minimal
        bitbake('core-image-minimal')

        # Boot qemu image & get rpm version
        proc_qemu = pexpect.spawn('runqemu qemux86 nographic')
        try:
            proc_qemu.expect('qemux86 login:', timeout=100)
            proc_qemu.sendline(self.root_user)
            proc_qemu.expect(self.prompt)
            proc_qemu.sendline('rpm --version')
            proc_qemu.expect(self.prompt)
        except Exception as e:
            try:
                killpg(proc_qemu.pid, signal.SIGTERM)
            except:
                pass
            self.fail('Failed to start qemu: %s' % e)

        found_rpm_version = proc_qemu.before

        # Make sure the retrieved rpm version is the expected one
        self.assertIn(rpm_version, found_rpm_version,
                      'RPM version is not {}, found instead {}.'.format(rpm_version, found_rpm_version))

        # Cleanup (close qemu)
        try:
            killpg(proc_qemu.pid, signal.SIGTERM)
        except:
            pass

    @testcase(1116)
    def test_clutter_image_can_be_built(self):
        """
        Summary:     Check if clutter image can be built
        Expected:    1. core-image-clutter can be built
        Product:     oe-core
        Author:      Ionut Chisanovici <ionutx.chisanovici@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """

        # Build a core-image-clutter
        bitbake('core-image-clutter')

    @testcase(1117)
    def test_wayland_support_in_image(self):
        """
        Summary:     Check Wayland support in image
        Expected:    1. Wayland image can be build
                     2. Wayland feature can be installed
        Product:     oe-core
        Author:      Ionut Chisanovici <ionutx.chisanovici@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """

        features = 'DISTRO_FEATURES_append = " wayland"\n'
        features += 'CORE_IMAGE_EXTRA_INSTALL += "wayland weston"'

        # Append 'features' to local.conf
        self.append_config(features)

        # Build a core-image-weston
        bitbake('core-image-weston')


class Gummiboot(oeSelfTest):

    meta_intel_dir = ''

    def setUpLocal(self):
        """
        Common setup for test cases: 1101, 1103
        """

        self.meta_intel_dir = get_bb_var('COREBASE') + '/meta-intel'
        meta_nuc_dir = self.meta_intel_dir + '/meta-nuc'
        meta_intel_repo = 'http://git.yoctoproject.org/git/meta-intel'

        # Delete meta_intel_dir
        system('rm -rf ' + self.meta_intel_dir)

        # Delete meta-intel dir even if the setUp fails
        self.add_command_to_tearDown('rm -rf ' + self.meta_intel_dir)

        # Clone meta-intel
        runCmd('git clone ' + meta_intel_repo + ' ' + self.meta_intel_dir)

        # Add meta-intel and meta-nuc layers in conf/bblayers.conf
        features = 'BBLAYERS += "' + self.meta_intel_dir + ' ' + meta_nuc_dir + '"'
        self.append_bblayers_config(features)

        # Set EFI_PROVIDER = "gummiboot" and MACHINE = "nuc" in conf/local.conf
        features = 'EFI_PROVIDER = "gummiboot"\n'
        features += 'MACHINE = "nuc"'
        self.append_config(features)

        # Run "bitbake syslinux syslinux-native parted-native dosfstools-native mtools-native core-image-minimal "
        # to build a nuc/efi gummiboot image

        bitbake('syslinux syslinux-native parted-native dosfstools-native mtools-native core-image-minimal')

    @testcase(1101)
    def test_efi_gummiboot_images_can_be_build(self):
        """
        Summary:     Check if efi/gummiboot images can be buit
        Expected:    1. File gummibootx64.efi should be available in build/tmp/deploy/images/nuc
                     2. Efi/gummiboot images can be built
        Product:     oe-core
        Author:      Ionut Chisanovici <ionutx.chisanovici@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """

        look_for_file = 'gummibootx64.efi'
        file_location = get_bb_var('COREBASE') + '/build/tmp/deploy/images/nuc/'

        found = isfile(file_location + look_for_file)
        self.assertTrue(found, 'File {} not found under {}.'.format(look_for_file, file_location))

    @testcase(1103)
    def test_wic_command_can_create_efi_gummiboot_installation_images(self):
        """
        Summary:     Check that wic command can create efi/gummiboot installation images
        Expected:    A .direct file in folder /var/tmp/wic/ must be created.
        Product:     oe-core
        Author:      Ionut Chisanovici <ionutx.chisanovici@intel.com>
        AutomatedBy: Daniel Istrate <daniel.alexandrux.istrate@intel.com>
        """

        # Create efi/gummiboot installation images
        wic_create_cmd = 'wic create mkgummidisk -e core-image-minimal'
        runCmd(wic_create_cmd)

        # Verify that a .direct file was created
        direct_file = '/var/tmp/wic/build/*.direct'
        ret = glob.glob(direct_file)
        self.assertEqual(1, len(ret), 'Failed to find the .direct file')
