#
# SPDX-License-Identifier: MIT
#

from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import runCmd, bitbake, get_bb_var, runqemu
import os
import json

class FitImageTests(OESelftestTestCase):

    def test_fit_image(self):
        """
        Summary:     Check if FIT image and Image Tree Source (its) are built
                     and the Image Tree Source has the correct fields.
        Expected:    1. fitImage and fitImage-its can be built
                     2. The type, load address, entrypoint address and
                     default values of kernel and ramdisk are as expected
                     in the Image Tree Source. Not all the fields are tested,
                     only the key fields that wont vary between different
                     architectures.
        Product:     oe-core
        Author:      Usama Arif <usama.arif@arm.com>
        """
        config = """
# Enable creation of fitImage
KERNEL_IMAGETYPE = "Image"
KERNEL_IMAGETYPES += " fitImage "
KERNEL_CLASSES = " kernel-fitimage "

# RAM disk variables including load address and entrypoint for kernel and RAM disk
IMAGE_FSTYPES += "cpio.gz"
INITRAMFS_IMAGE = "core-image-minimal"
UBOOT_RD_LOADADDRESS = "0x88000000"
UBOOT_RD_ENTRYPOINT = "0x88000000"
UBOOT_LOADADDRESS = "0x80080000"
UBOOT_ENTRYPOINT = "0x80080000"
FIT_DESC = "A model description"
"""
        self.write_config(config)

        # fitImage is created as part of linux recipe
        bitbake("virtual/kernel")

        image_type = "core-image-minimal"
        deploy_dir_image = get_bb_var('DEPLOY_DIR_IMAGE')
        machine = get_bb_var('MACHINE')
        fitimage_its_path = os.path.join(deploy_dir_image,
            "fitImage-its-%s-%s-%s" % (image_type, machine, machine))
        fitimage_path = os.path.join(deploy_dir_image,
            "fitImage-%s-%s-%s" % (image_type, machine, machine))

        self.assertTrue(os.path.exists(fitimage_its_path),
            "%s image tree source doesn't exist" % (fitimage_its_path))
        self.assertTrue(os.path.exists(fitimage_path),
            "%s FIT image doesn't exist" % (fitimage_path))

        # Check that the type, load address, entrypoint address and default
        # values for kernel and ramdisk in Image Tree Source are as expected.
        # The order of fields in the below array is important. Not all the
        # fields are tested, only the key fields that wont vary between
        # different architectures.
        its_field_check = [
            'description = "A model description";',
            'type = "kernel";',
            'load = <0x80080000>;',
            'entry = <0x80080000>;',
            'type = "ramdisk";',
            'load = <0x88000000>;',
            'entry = <0x88000000>;',
            'default = "conf@1";',
            'kernel = "kernel@1";',
            'ramdisk = "ramdisk@1";'
            ]

        with open(fitimage_its_path) as its_file:
            field_index = 0
            for line in its_file:
                if field_index == len(its_field_check):
                    break
                if its_field_check[field_index] in line:
                    field_index +=1

        if field_index != len(its_field_check): # if its equal, the test passed
            self.assertTrue(field_index == len(its_field_check),
                "Fields in Image Tree Source File %s did not match, error in finding %s"
                % (fitimage_its_path, its_field_check[field_index]))
