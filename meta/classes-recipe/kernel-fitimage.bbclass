#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

inherit kernel-uboot kernel-artifact-names uboot-config
require conf/image-fitimage.conf

def get_fit_replacement_type(d):
    kerneltypes = d.getVar('KERNEL_IMAGETYPES') or ""
    replacementtype = ""
    if 'fitImage' in kerneltypes.split():
        uarch = d.getVar("UBOOT_ARCH")
        if uarch == "arm64":
            replacementtype = "Image"
        elif uarch == "riscv":
            replacementtype = "Image"
        elif uarch == "mips":
            replacementtype = "vmlinuz.bin"
        elif uarch == "x86":
            replacementtype = "bzImage"
        elif uarch == "microblaze":
            replacementtype = "linux.bin"
        else:
            replacementtype = "zImage"
    return replacementtype

KERNEL_IMAGETYPE_REPLACEMENT ?= "${@get_fit_replacement_type(d)}"
DEPENDS:append = " \
    ${@'u-boot-tools-native dtc-native' if 'fitImage' in (d.getVar('KERNEL_IMAGETYPES') or '').split() else ''} \
    ${@'kernel-signing-keys-native' if d.getVar('FIT_GENERATE_KEYS') == '1' else ''} \
"

python __anonymous () {
    # Override KERNEL_IMAGETYPE_FOR_MAKE variable, which is internal
    # to kernel.bbclass . We have to override it, since we pack zImage
    # (at least for now) into the fitImage .
    typeformake = d.getVar("KERNEL_IMAGETYPE_FOR_MAKE") or ""
    if 'fitImage' in typeformake.split():
        d.setVar('KERNEL_IMAGETYPE_FOR_MAKE', typeformake.replace('fitImage', d.getVar('KERNEL_IMAGETYPE_REPLACEMENT')))

    image = d.getVar('INITRAMFS_IMAGE')
    if image and not bb.utils.to_boolean(d.getVar('INITRAMFS_IMAGE_BUNDLE')):
        if d.getVar('INITRAMFS_MULTICONFIG'):
            mc = d.getVar('BB_CURRENT_MC')
            d.appendVarFlag('do_assemble_fitimage_initramfs', 'mcdepends', ' mc:' + mc + ':${INITRAMFS_MULTICONFIG}:${INITRAMFS_IMAGE}:do_image_complete')
        else:
            d.appendVarFlag('do_assemble_fitimage_initramfs', 'depends', ' ${INITRAMFS_IMAGE}:do_image_complete')

    #check if there are any dtb providers
    providerdtb = d.getVar("PREFERRED_PROVIDER_virtual/dtb")
    if providerdtb:
        d.appendVarFlag('do_assemble_fitimage', 'depends', ' virtual/dtb:do_populate_sysroot')
        d.appendVarFlag('do_assemble_fitimage_initramfs', 'depends', ' virtual/dtb:do_populate_sysroot')
        d.setVar('EXTERNAL_KERNEL_DEVICETREE', "${RECIPE_SYSROOT}/boot/devicetree")
}

def fitimage_assemble(d, itsfile, fitname, ramdiskcount):
    import shutil
    import glob
    import oe.fitimage

    DTBS=""
    default_dtb_image=""

    for f in [itsfile, os.path.join("arch", d.getVar("ARCH"), "boot", fitname)]:
        if os.path.exists(f):
            os.remove(f)

    root_node = oe.fitimage.ItsNodeRootKernel(
        d.getVar("FIT_DESC"), d.getVar("FIT_ADDRESS_CELLS"),
        d.getVar('HOST_PREFIX'), d.getVar('UBOOT_ARCH'),  d.getVar("FIT_CONF_PREFIX"),
        d.getVar('UBOOT_SIGN_ENABLE') == "1", d.getVar("UBOOT_SIGN_KEYDIR"),
        d.getVar("UBOOT_MKIMAGE"), d.getVar("UBOOT_MKIMAGE_DTCOPTS"),
        d.getVar("UBOOT_MKIMAGE_SIGN"), d.getVar("UBOOT_MKIMAGE_SIGN_ARGS"),
        d.getVar('FIT_HASH_ALG'), d.getVar('FIT_SIGN_ALG'), d.getVar('FIT_PAD_ALG'),
        d.getVar('UBOOT_SIGN_KEYNAME'),
        d.getVar('FIT_SIGN_INDIVIDUAL') == "1", d.getVar('UBOOT_SIGN_IMG_KEYNAME')
    )

    #
    # Step 1: Prepare a kernel image section.
    #
    linux_comp = uboot_prep_kimage_py(d)
    root_node.fitimage_emit_section_kernel("kernel-1", "linux.bin", linux_comp,
        d.getVar('UBOOT_LOADADDRESS'), d.getVar('UBOOT_ENTRYPOINT'),
        d.getVar('UBOOT_MKIMAGE_KERNEL_TYPE'), d.getVar("UBOOT_ENTRYSYMBOL"))

    #
    # Step 2: Prepare a DTB image section
    #
    kernel_devicetree = d.getVar('KERNEL_DEVICETREE')
    external_kernel_devicetree = d.getVar("EXTERNAL_KERNEL_DEVICETREE")
    if kernel_devicetree:
        for DTB in kernel_devicetree.split():
            if "/dts/" in DTB:
                bb.warn(f"{DTB} contains the full path to the dts file, but only the dtb name should be used.")
                DTB = os.path.basename(DTB).replace(".dts", ".dtb")

            # Skip DTB if it's also provided in EXTERNAL_KERNEL_DEVICETREE
            if external_kernel_devicetree:
                ext_dtb_path = os.path.join(external_kernel_devicetree, DTB)
                if os.path.exists(ext_dtb_path) and os.path.getsize(ext_dtb_path) > 0:
                    continue

            DTB_PATH = os.path.join(d.getVar("KERNEL_OUTPUT_DIR"), "dts", DTB)
            if not os.path.exists(DTB_PATH):
                DTB_PATH = os.path.join(d.getVar("KERNEL_OUTPUT_DIR"), DTB)

            # Strip off the path component from the filename
            if not oe.types.boolean(d.getVar("KERNEL_DTBVENDORED")):
                DTB = os.path.basename(DTB)

            # Set the default dtb image if it exists in the devicetree.
            if d.getVar("FIT_CONF_DEFAULT_DTB") == DTB:
                default_dtb_image = DTB.replace("/", "_")

            DTB = DTB.replace("/", "_")

            # Skip DTB if we've picked it up previously
            if DTB in DTBS.split():
                continue

            DTBS += " " + DTB

            root_node.fitimage_emit_section_dtb(DTB, DTB_PATH,
                d.getVar("UBOOT_DTB_LOADADDRESS"), d.getVar("UBOOT_DTBO_LOADADDRESS"))

    if external_kernel_devicetree:
        dtb_files = []
        for ext in ['*.dtb', '*.dtbo']:
            dtb_files.extend(sorted(glob.glob(os.path.join(external_kernel_devicetree, ext))))

        for dtb_path in dtb_files:
            dtb_name = os.path.relpath(dtb_path, external_kernel_devicetree)
            dtb_name_underscore = dtb_name.replace('/', '_')

            # Set the default dtb image if it exists in the devicetree.
            if d.getVar("FIT_CONF_DEFAULT_DTB") == dtb_name:
                default_dtb_image = dtb_name_underscore

            # Skip DTB/DTBO if we've picked it up previously
            if dtb_name_underscore in DTBS.split():
                continue

            DTBS += " " + dtb_name_underscore

            # For symlinks, add a configuration node that refers to the DTB image node to which the symlink points
            symlink_target = oe.fitimage.symlink_points_below(dtb_name, external_kernel_devicetree)
            if symlink_target:
                root_node.fitimage_emit_section_dtb_alias(dtb_name, symlink_target, True)
            # For real DTB files add an image node and a configuration node
            else:
                root_node.fitimage_emit_section_dtb(dtb_name_underscore, dtb_path,
                    d.getVar("UBOOT_DTB_LOADADDRESS"), d.getVar("UBOOT_DTBO_LOADADDRESS"), True)

    if d.getVar("FIT_CONF_DEFAULT_DTB") and not default_dtb_image:
        bb.warn("%s is not available in the list of device trees." % d.getVar('FIT_CONF_DEFAULT_DTB'))

    #
    # Step 3: Prepare a u-boot script section
    #
    fit_uboot_env = d.getVar("FIT_UBOOT_ENV")
    if fit_uboot_env:
        unpack_dir = d.getVar("UNPACKDIR")
        shutil.copy(os.path.join(unpack_dir, fit_uboot_env), fit_uboot_env)
        root_node.fitimage_emit_section_boot_script("bootscr-"+fit_uboot_env , fit_uboot_env)

    #
    # Step 4: Prepare a setup section. (For x86)
    #
    setup_bin_path = os.path.join(d.getVar("KERNEL_OUTPUT_DIR"), "setup.bin")
    if os.path.exists(setup_bin_path):
        root_node.fitimage_emit_section_setup("setup-1", setup_bin_path)

    #
    # Step 5: Prepare a ramdisk section.
    #
    if ramdiskcount == 1 and d.getVar("INITRAMFS_IMAGE_BUNDLE") != "1":
        # Find and use the first initramfs image archive type we find
        found = False
        for img in d.getVar("FIT_SUPPORTED_INITRAMFS_FSTYPES").split():
            initramfs_path = os.path.join(d.getVar("DEPLOY_DIR_IMAGE"), "%s.%s" % (d.getVar('INITRAMFS_IMAGE_NAME'), img))
            if os.path.exists(initramfs_path):
                bb.note("Found initramfs image: " + initramfs_path)
                found = True
                root_node.fitimage_emit_section_ramdisk("ramdisk-%d" % ramdiskcount, initramfs_path,
                    d.getVar('INITRAMFS_IMAGE'),
                    d.getVar("UBOOT_RD_LOADADDRESS"),
                    d.getVar("UBOOT_RD_ENTRYPOINT"))
                break
            else:
                bb.note("Did not find initramfs image: " + initramfs_path)

        if not found:
            bb.fatal("Could not find a valid initramfs type for %s, the supported types are: %s" % (d.getVar('INITRAMFS_IMAGE_NAME'), d.getVar('FIT_SUPPORTED_INITRAMFS_FSTYPES')))

    # Generate the configuration section
    root_node.fitimage_emit_section_config(default_dtb_image)

    #
    # Write the ITS file
    root_node.write_its_file(itsfile)

    #
    # Step 7: Assemble the image
    #
    fitfile = os.path.join(d.getVar("KERNEL_OUTPUT_DIR"), fitname)
    root_node.run_mkimage_assemble(itsfile, fitfile)

    #
    # Step 8: Sign the image if required
    #
    root_node.run_mkimage_sign(fitfile)


python do_assemble_fitimage() {
    if "fitImage" in d.getVar("KERNEL_IMAGETYPES").split():
        os.chdir(d.getVar("B"))
        fitimage_assemble(d, "fit-image.its", "fitImage-none", "")
        if d.getVar("INITRAMFS_IMAGE_BUNDLE") != "1":
            link_name = os.path.join(d.getVar("B"), d.getVar("KERNEL_OUTPUT_DIR"), "fitImage")
            if os.path.islink(link_name):
                os.unlink(link_name)
            os.symlink("fitImage-none", link_name)
}

addtask assemble_fitimage before do_install after do_compile

SYSROOT_DIRS:append = " /sysroot-only"
do_install:append() {
	if echo ${KERNEL_IMAGETYPES} | grep -wq "fitImage" && \
		[ "${UBOOT_SIGN_ENABLE}" = "1" ]; then
		install -D ${B}/${KERNEL_OUTPUT_DIR}/fitImage-none ${D}/sysroot-only/fitImage
	fi
}

python do_assemble_fitimage_initramfs() {
    if "fitImage" in d.getVar("KERNEL_IMAGETYPES").split() and d.getVar("INITRAMFS_IMAGE"):
        os.chdir(d.getVar("B"))
        if d.getVar("INITRAMFS_IMAGE_BUNDLE") == "1":
            fitimage_assemble(d, "fit-image-%s.its" % d.getVar("INITRAMFS_IMAGE"), "fitImage-bundle", "")
            link_name =  os.path.join(d.getVar("B"), d.getVar("KERNEL_OUTPUT_DIR"), "fitImage")
            if os.path.islink(link_name):
                os.unlink(link_name)
            os.symlink("fitImage-bundle", link_name)
        else:
            fitimage_assemble(d, "fit-image-%s.its" % d.getVar("INITRAMFS_IMAGE"), "fitImage-%s" % d.getVar("INITRAMFS_IMAGE"), 1)
}

addtask assemble_fitimage_initramfs before do_deploy after do_bundle_initramfs

kernel_do_deploy[vardepsexclude] = "DATETIME"
kernel_do_deploy:append() {
	# Update deploy directory
	if echo ${KERNEL_IMAGETYPES} | grep -wq "fitImage"; then

		if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
			bbnote "Copying fit-image.its source file..."
			install -m 0644 ${B}/fit-image.its "$deployDir/fitImage-its-${KERNEL_FIT_NAME}.its"
			if [ -n "${KERNEL_FIT_LINK_NAME}" ] ; then
				ln -snf fitImage-its-${KERNEL_FIT_NAME}.its "$deployDir/fitImage-its-${KERNEL_FIT_LINK_NAME}"
			fi

			bbnote "Copying linux.bin file..."
			install -m 0644 ${B}/linux.bin $deployDir/fitImage-linux.bin-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT}
			if [ -n "${KERNEL_FIT_LINK_NAME}" ] ; then
				ln -snf fitImage-linux.bin-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT} "$deployDir/fitImage-linux.bin-${KERNEL_FIT_LINK_NAME}"
			fi
		fi

		if [ -n "${INITRAMFS_IMAGE}" ]; then
			bbnote "Copying fit-image-${INITRAMFS_IMAGE}.its source file..."
			install -m 0644 ${B}/fit-image-${INITRAMFS_IMAGE}.its "$deployDir/fitImage-its-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}.its"
			if [ -n "${KERNEL_FIT_LINK_NAME}" ] ; then
				ln -snf fitImage-its-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}.its "$deployDir/fitImage-its-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_LINK_NAME}"
			fi

			if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
				bbnote "Copying fitImage-${INITRAMFS_IMAGE} file..."
				install -m 0644 ${B}/${KERNEL_OUTPUT_DIR}/fitImage-${INITRAMFS_IMAGE} "$deployDir/fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT}"
				if [ -n "${KERNEL_FIT_LINK_NAME}" ] ; then
					ln -snf fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT} "$deployDir/fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_LINK_NAME}"
				fi
			fi
		fi
	fi
}
