
inherit kernel-artifact-names uboot-config deploy
require conf/image-fitimage.conf

S = "${UNPACKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# This bbclass requires KERNEL_CLASSES += "kernel-fit-extra-artifacts"
EXCLUDE_FROM_WORLD = "1"

DEPENDS += "\
    u-boot-tools-native dtc-native \
    ${@'kernel-signing-keys-native' if d.getVar('FIT_GENERATE_KEYS') == '1' else ''} \
"

python () {
    image = d.getVar('INITRAMFS_IMAGE')
    if image and d.getVar('INITRAMFS_IMAGE_BUNDLE') != '1':
        if d.getVar('INITRAMFS_MULTICONFIG'):
            mc = d.getVar('BB_CURRENT_MC')
            d.appendVarFlag('do_compile', 'mcdepends', ' mc:' + mc + ':${INITRAMFS_MULTICONFIG}:${INITRAMFS_IMAGE}:do_image_complete')
        else:
            d.appendVarFlag('do_compile', 'depends', ' ${INITRAMFS_IMAGE}:do_image_complete')

    #check if there are any dtb providers
    providerdtb = d.getVar("PREFERRED_PROVIDER_virtual/dtb")
    if providerdtb:
        d.appendVarFlag('do_compile', 'depends', ' virtual/dtb:do_populate_sysroot')
        d.setVar('EXTERNAL_KERNEL_DEVICETREE', "${RECIPE_SYSROOT}/boot/devicetree")

    # Parse loadables, additional kernels and additional configurations
    for items_var, flag_vars in (
            ('FIT_LOADABLES', ['FIT_LOADABLE_ARCH', 'FIT_LOADABLE_COMPRESSION',
                               'FIT_LOADABLE_DESCRIPTION', 'FIT_LOADABLE_ENTRYPOINT',
                               'FIT_LOADABLE_FILENAME', 'FIT_LOADABLE_LOADADDRESS',
                               'FIT_LOADABLE_OS', 'FIT_LOADABLE_TYPE']),
            ('FIT_KERNELS', ['FIT_KERNEL_COMPRESSION', 'FIT_KERNEL_DEPLOYDIR',
                             'FIT_KERNEL_DESCRIPTION', 'FIT_KERNEL_ENTRYPOINT',
                             'FIT_KERNEL_FILENAME', 'FIT_KERNEL_LOADADDRESS',
                             'FIT_KERNEL_MULTICONFIG', 'FIT_KERNEL_RECIPE',
                             'FIT_KERNEL_TYPE']),
            ('FIT_CONFIGS', ['FIT_CONFIG_DTB', 'FIT_CONFIG_KERNEL'])):
        items = d.getVar(items_var)
        if items:
            # Check that the configuration variables are not set directly,
            # then assign them a synthetic value for testability purposes
            for v in flag_vars:
                if d.getVar(v):
                    raise bb.parse.SkipRecipe("You cannot use %s as a variable, you can only set flags." % v)

                synt_value = " ? ".join([ d.getVarFlag(v, item, False) or "" for item in items.split() ])
                d.setVar(v, synt_value)

    # Additional kernels may be deployed by another recipe, by a recipe of
    # another multiconfig or be provided as prebuilt files
    kernels = (d.getVar('FIT_KERNELS') or "").split()
    if "kernel-1" in kernels:
        bb.fatal("FIT_KERNELS must not contain kernel-1, it is reserved for virtual/kernel")
    duplicates = sorted(set(k for k in kernels if kernels.count(k) > 1))
    if duplicates:
        bb.fatal("FIT_KERNELS contains duplicate entries: %s" % ' '.join(duplicates))
    for kernel in kernels:
        recipe = d.getVarFlag('FIT_KERNEL_RECIPE', kernel)
        mc = d.getVarFlag('FIT_KERNEL_MULTICONFIG', kernel)
        if mc:
            if not d.getVarFlag('FIT_KERNEL_DEPLOYDIR', kernel):
                bb.fatal("FIT_KERNEL_DEPLOYDIR[%s] must be set if FIT_KERNEL_MULTICONFIG[%s] is set" % (kernel, kernel))
            d.appendVarFlag('do_compile', 'mcdepends', ' mc:%s:%s:%s:do_deploy' %
                            (d.getVar('BB_CURRENT_MC'), mc, recipe or 'virtual/kernel'))
        elif recipe:
            d.appendVarFlag('do_compile', 'depends', ' %s:do_deploy' % recipe)
        else:
            if not d.getVarFlag('FIT_KERNEL_FILENAME', kernel):
                bb.fatal("FIT_KERNEL_FILENAME[%s] must be set if neither FIT_KERNEL_RECIPE[%s] nor FIT_KERNEL_MULTICONFIG[%s] is set" % (kernel, kernel, kernel))
            kernel_path = fit_kernel_path(d, kernel)
            d.appendVarFlag('do_compile', 'file-checksums', ' %s:%s' % (kernel_path, os.path.exists(kernel_path)))
}

do_configure[noexec] = "1"

INITRAMFS_DEPLOY_DIR_IMAGE ?= "${DEPLOY_DIR_IMAGE}"
UBOOT_MKIMAGE_KERNEL_TYPE ?= "kernel"
KERNEL_IMAGEDEST ?= "/boot"

FIT_KERNEL_SIGN_ENABLE ?= "${UBOOT_SIGN_ENABLE}"
FIT_KERNEL_SIGN_KEYNAME ?= "${UBOOT_SIGN_KEYNAME}"
FIT_KERNEL_SIGN_KEYDIR ?= "${UBOOT_SIGN_KEYDIR}"

def apply_dtbvendored(d,dtb):
    if d.getVar('KERNEL_DTBVENDORED') != "1":
        mapped_name = os.path.basename(dtb)
    else:
        mapped_name = dtb.replace('/', '_')
    return mapped_name

def fit_kernel_path(d, kernel):
    filename = d.getVarFlag('FIT_KERNEL_FILENAME', kernel) or "linux.bin"
    deploydir = d.getVarFlag('FIT_KERNEL_DEPLOYDIR', kernel) or d.getVar('DEPLOY_DIR_IMAGE')
    return os.path.join(deploydir, filename)

def fit_kernel_deployed_files(d, recipe):
    """Return the real paths of all files deployed by do_deploy of recipe (PN or PROVIDES) in this multiconfig"""
    import bb.runqueue
    mc = d.getVar('BB_CURRENT_MC') or ""
    taskdepdata = d.getVar('BB_TASKDEPDATA', False)
    pns = set(dep.pn for tid, dep in taskdepdata.items()
              if dep.taskname == "do_deploy" and bb.runqueue.mc_from_tid(tid) == mc and
                 (dep.pn == recipe or recipe in dep.provides))
    if len(pns) != 1:
        bb.fatal("Could not identify the do_deploy dependency providing %s (found: %s)" % (recipe, ' '.join(sorted(pns)) or "none"))
    pn = pns.pop()
    for arch in d.getVar('SSTATE_ARCHS').split():
        manifest = os.path.join(d.getVar('SSTATE_MANIFESTS'), "manifest-%s-%s.deploy" % (arch, pn))
        if os.path.exists(manifest):
            with open(manifest) as f:
                return set(os.path.realpath(line) for line in f.read().splitlines() if line and not line.endswith('/'))
    bb.fatal("No deploy manifest found for %s" % pn)
fit_kernel_deployed_files[vardepsexclude] = "BB_TASKDEPDATA SSTATE_ARCHS"

python do_compile() {
    import shutil
    import oe.fitimage

    itsfile = "fit-image.its"
    fitname = "fitImage"
    kernel_deploydir = d.getVar('DEPLOY_DIR_IMAGE')
    kernel_deploysubdir = d.getVar('KERNEL_DEPLOYSUBDIR')
    if kernel_deploysubdir:
        kernel_deploydir = os.path.join(kernel_deploydir, kernel_deploysubdir)

    # Collect all the its nodes before the its file is generated and mkimage gets executed
    root_node = oe.fitimage.ItsNodeRootKernel(
        d.getVar("FIT_DESC"), d.getVar("FIT_ADDRESS_CELLS"),
        d.getVar('HOST_PREFIX'), oe.kernel.map_uboot_arch(d), d.getVar('FIT_OS'),
        d.getVar("FIT_CONF_PREFIX"),
        oe.types.boolean(d.getVar('FIT_KERNEL_SIGN_ENABLE')), d.getVar("FIT_KERNEL_SIGN_KEYDIR"),
        d.getVar("UBOOT_MKIMAGE"), d.getVar("UBOOT_MKIMAGE_DTCOPTS"),
        d.getVar('FIT_MKIMAGE_EXTRA_OPTS'),
        d.getVar("UBOOT_MKIMAGE_SIGN"), d.getVar("UBOOT_MKIMAGE_SIGN_ARGS"),
        d.getVar('FIT_HASH_ALG'), d.getVar('FIT_SIGN_ALG'), d.getVar('FIT_PAD_ALG'),
        d.getVar('FIT_KERNEL_SIGN_KEYNAME'),
        oe.types.boolean(d.getVar('FIT_SIGN_INDIVIDUAL')), d.getVar('UBOOT_SIGN_IMG_KEYNAME')
    )

    # Prepare a kernel image section.
    shutil.copyfile(os.path.join(kernel_deploydir, "linux.bin"), "linux.bin")
    with open(os.path.join(kernel_deploydir, "linux_comp")) as linux_comp_f:
        linux_comp = linux_comp_f.read()
    root_node.fitimage_emit_section_kernel("kernel-1", "linux.bin", linux_comp,
        d.getVar('UBOOT_LOADADDRESS'), d.getVar('UBOOT_ENTRYPOINT'),
        d.getVar('UBOOT_MKIMAGE_KERNEL_TYPE'), d.getVar("UBOOT_ENTRYSYMBOL"))

    # Prepare additional kernel image sections
    for kernel in d.getVar('FIT_KERNELS').split():
        recipe = d.getVarFlag('FIT_KERNEL_RECIPE', kernel)
        mc = d.getVarFlag('FIT_KERNEL_MULTICONFIG', kernel)
        kernel_path = fit_kernel_path(d, kernel)
        if not os.path.exists(kernel_path):
            bb.fatal("File for kernel %s not found at %s" % (kernel, kernel_path))
        # Artifacts of another multiconfig cannot be verified, its FIT_KERNEL_DEPLOYDIR is trusted
        deployed = None
        if recipe and not mc:
            deployed = fit_kernel_deployed_files(d, recipe)
            if os.path.realpath(kernel_path) not in deployed:
                bb.fatal("File for kernel %s at %s is not deployed by %s" % (kernel, kernel_path, recipe))
        kernel_file = "linux-%s.bin" % kernel
        shutil.copyfile(kernel_path, kernel_file)

        kernel_comp = d.getVarFlag('FIT_KERNEL_COMPRESSION', kernel)
        if not kernel_comp:
            kernel_comp = "none"
            # linux_comp only describes linux.bin written by kernel-fit-extra-artifacts of the same recipe
            if (recipe or mc) and os.path.basename(kernel_path) == "linux.bin":
                kernel_comp_path = os.path.join(os.path.dirname(kernel_path), "linux_comp")
                if not os.path.exists(kernel_comp_path) or \
                        (deployed is not None and os.path.realpath(kernel_comp_path) not in deployed):
                    bb.fatal("%s not found or not deployed by the kernel recipe, set FIT_KERNEL_COMPRESSION[%s]" % (kernel_comp_path, kernel))
                with open(kernel_comp_path) as kernel_comp_f:
                    kernel_comp = kernel_comp_f.read().strip()
        root_node.fitimage_emit_section_kernel(kernel, kernel_file, kernel_comp,
            d.getVarFlag('FIT_KERNEL_LOADADDRESS', kernel) or d.getVar('UBOOT_LOADADDRESS'),
            d.getVarFlag('FIT_KERNEL_ENTRYPOINT', kernel) or d.getVar('UBOOT_ENTRYPOINT'),
            d.getVarFlag('FIT_KERNEL_TYPE', kernel) or d.getVar('UBOOT_MKIMAGE_KERNEL_TYPE'),
            description=d.getVarFlag('FIT_KERNEL_DESCRIPTION', kernel) or "Linux kernel")

    # Prepare a DTB image section
    kernel_devicetree = d.getVar('KERNEL_DEVICETREE')
    external_kernel_devicetree = d.getVar("EXTERNAL_KERNEL_DEVICETREE")
    if kernel_devicetree:
        for dtb in kernel_devicetree.split():
            # In deploy_dir the DTBs are without sub-directories also with KERNEL_DTBVENDORED = "1"
            dtb_name = os.path.basename(dtb)
            # With vendored DTs, use the DT name as listed in KERNEL_DEVICETREE
            # and replace any path separators with underscores, this behaves the
            # same way as KERNEL_DTBVENDORED = "1" did in OE 5.0 and older.
            conf_name = apply_dtbvendored(d, dtb)

            # Skip DTB if it's also provided in EXTERNAL_KERNEL_DEVICETREE directory
            if external_kernel_devicetree:
                ext_dtb_path = os.path.join(external_kernel_devicetree, dtb_name)
                if os.path.exists(ext_dtb_path) and os.path.getsize(ext_dtb_path) > 0:
                    continue

            # Copy the dtb or dtbo file into the FIT image assembly directory
            shutil.copyfile(os.path.join(kernel_deploydir, dtb_name), dtb_name)
            root_node.fitimage_emit_section_dtb(conf_name, dtb_name,
                d.getVar("UBOOT_DTB_LOADADDRESS"), d.getVar("UBOOT_DTBO_LOADADDRESS"))

    if external_kernel_devicetree:
        # iterate over all .dtb and .dtbo files in the external kernel devicetree directory
        # and copy them to the FIT image assembly directory
        for dtb_name in sorted(os.listdir(external_kernel_devicetree)):
            if dtb_name.endswith('.dtb') or dtb_name.endswith('.dtbo'):
                dtb_path = os.path.join(external_kernel_devicetree, dtb_name)

                # For symlinks, add a configuration node that refers to the DTB image node to which the symlink points
                symlink_target = oe.fitimage.symlink_points_below(dtb_name, external_kernel_devicetree)
                if symlink_target:
                    root_node.fitimage_emit_section_dtb_alias(dtb_name, symlink_target, True)
                # For real DTB files add an image node and a configuration node
                else:
                    shutil.copyfile(dtb_path, dtb_name)
                    root_node.fitimage_emit_section_dtb(dtb_name, dtb_name,
                        d.getVar("UBOOT_DTB_LOADADDRESS"), d.getVar("UBOOT_DTBO_LOADADDRESS"), True)

    # Prepare a u-boot script section
    fit_uboot_env = d.getVar("FIT_UBOOT_ENV")
    if fit_uboot_env:
        root_node.fitimage_emit_section_boot_script("bootscr-"+fit_uboot_env , fit_uboot_env)

    # Prepare a setup section (For x86)
    setup_bin_path = os.path.join(kernel_deploydir, "setup.bin")
    if os.path.exists(setup_bin_path):
        shutil.copyfile(setup_bin_path, "setup.bin")
        root_node.fitimage_emit_section_setup("setup-1", "setup.bin")

    # Prepare a ramdisk section.
    initramfs_image = d.getVar('INITRAMFS_IMAGE')
    if initramfs_image and d.getVar("INITRAMFS_IMAGE_BUNDLE") != '1':
        # Find and use the first initramfs image archive type we find
        found = False
        for img in d.getVar("FIT_SUPPORTED_INITRAMFS_FSTYPES").split():
            initramfs_path = os.path.join(d.getVar("INITRAMFS_DEPLOY_DIR_IMAGE"), "%s.%s" % (d.getVar('INITRAMFS_IMAGE_NAME'), img))
            if os.path.exists(initramfs_path):
                bb.note("Found initramfs image: " + initramfs_path)
                found = True
                root_node.fitimage_emit_section_ramdisk("ramdisk-1", initramfs_path,
                    initramfs_image,
                    d.getVar("UBOOT_RD_LOADADDRESS"),
                    d.getVar("UBOOT_RD_ENTRYPOINT"))
                break
            else:
                bb.note("Did not find initramfs image: " + initramfs_path)

        if not found:
            bb.fatal("Could not find a valid initramfs type for %s, the supported types are: %s" % (d.getVar('INITRAMFS_IMAGE_NAME'), d.getVar('FIT_SUPPORTED_INITRAMFS_FSTYPES')))

    #
    # Prepare loadables sections
    #
    for loadable in d.getVar('FIT_LOADABLES').split():
        loadable_file = d.getVarFlag('FIT_LOADABLE_FILENAME', loadable)
        if not loadable_file:
            bb.fatal("File for loadable %s not specified through FIT_LOADABLE_FILENAME[%s]" % (loadable, loadable))

        loadable_loadaddress = d.getVarFlag('FIT_LOADABLE_LOADADDRESS', loadable)
        if not loadable_loadaddress:
            bb.fatal("Load address for loadable %s not specified through FIT_LOADABLE_LOADADDRESS[%s]" % (loadable, loadable))

        # Optional parameters
        loadable_arch = d.getVarFlag('FIT_LOADABLE_ARCH', loadable)
        loadable_compression = d.getVarFlag('FIT_LOADABLE_COMPRESSION', loadable)
        loadable_description = d.getVarFlag('FIT_LOADABLE_DESCRIPTION', loadable) or ("%s loadable" % loadable)
        loadable_entrypoint = d.getVarFlag('FIT_LOADABLE_ENTRYPOINT', loadable)
        loadable_os = d.getVarFlag('FIT_LOADABLE_OS', loadable)
        loadable_type = d.getVarFlag('FIT_LOADABLE_TYPE', loadable)

        # Check if loadable artifact exists
        loadable_path = os.path.join(d.getVar("DEPLOY_DIR_IMAGE"), loadable_file)
        if not os.path.exists(loadable_path):
            bb.fatal("File for loadable %s not found at %s" % (loadable, loadable_path))

        root_node.fitimage_emit_section_loadable(loadable,
                                                 loadable_path, loadable_type,
                                                 loadable_description,
                                                 loadable_compression,
                                                 loadable_arch, loadable_os,
                                                 loadable_loadaddress,
                                                 loadable_entrypoint)

    # Figure out if we have a default dtb and if we need to honor the
    # KERNEL_DTBVENDORED variable to tweak the name to match what will be
    # in the fitImage.
    default_dtb = d.getVar("FIT_CONF_DEFAULT_DTB")
    if default_dtb:
        default_dtb = apply_dtbvendored(d, default_dtb)

    # Generate the configuration section
    root_node.fitimage_emit_section_config(default_dtb, d.getVar("FIT_CONF_MAPPINGS"))

    # Generate additional configurations, e.g. referring to additional kernels
    for conf in d.getVar('FIT_CONFIGS').split():
        conf_dtb = d.getVarFlag('FIT_CONFIG_DTB', conf)
        if conf_dtb:
            conf_dtb = apply_dtbvendored(d, conf_dtb)
        root_node.fitimage_emit_section_config_custom(conf,
            d.getVarFlag('FIT_CONFIG_KERNEL', conf), conf_dtb)

    fit_conf_default = d.getVar('FIT_CONF_DEFAULT')
    if fit_conf_default:
        root_node.fitimage_set_default_config(fit_conf_default)

    # Write the its file
    root_node.write_its_file(itsfile)

    # Assemble the FIT image
    root_node.run_mkimage_assemble(itsfile, fitname)

    # Sign the FIT image if required
    root_node.run_mkimage_sign(fitname)
}
do_compile[depends] += "virtual/kernel:do_deploy"

do_install() {
    install -d "${D}/${KERNEL_IMAGEDEST}"
    install -m 0644 "${B}/fitImage" "${D}/${KERNEL_IMAGEDEST}/fitImage"
}

FILES:${PN} = "${KERNEL_IMAGEDEST}"


do_deploy() {
    deploy_dir="${DEPLOYDIR}"
    if [ -n "${KERNEL_DEPLOYSUBDIR}" ]; then
        deploy_dir="${DEPLOYDIR}/${KERNEL_DEPLOYSUBDIR}"
    fi
    install -d "$deploy_dir"
    install -m 0644 "${B}/fitImage" "$deploy_dir/fitImage"
    install -m 0644 "${B}/fit-image.its" "$deploy_dir/fit-image.its"

    if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
        ln -snf fit-image.its "$deploy_dir/fitImage-its-${KERNEL_FIT_NAME}.its"
        if [ -n "${KERNEL_FIT_LINK_NAME}" ] ; then
            ln -snf fit-image.its "$deploy_dir/fitImage-its-${KERNEL_FIT_LINK_NAME}"
        fi
    fi

    if [ -n "${INITRAMFS_IMAGE}" ]; then
        ln -snf fit-image.its "$deploy_dir/fitImage-its-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}.its"
        if [ -n "${KERNEL_FIT_LINK_NAME}" ]; then
            ln -snf fit-image.its "$deploy_dir/fitImage-its-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_LINK_NAME}"
        fi

        if [ "${INITRAMFS_IMAGE_BUNDLE}" != "1" ]; then
            ln -snf fitImage "$deploy_dir/fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}${KERNEL_FIT_BIN_EXT}"
            if [ -n "${KERNEL_FIT_LINK_NAME}" ] ; then
                ln -snf fitImage "$deploy_dir/fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_LINK_NAME}"
            fi
        fi
    fi
}
addtask deploy after do_compile before do_build
