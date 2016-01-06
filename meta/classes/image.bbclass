inherit rootfs_${IMAGE_PKGTYPE}

inherit populate_sdk_ext

TOOLCHAIN_TARGET_TASK += "${PACKAGE_INSTALL}"
TOOLCHAIN_TARGET_TASK_ATTEMPTONLY += "${PACKAGE_INSTALL_ATTEMPTONLY}"
POPULATE_SDK_POST_TARGET_COMMAND += "rootfs_sysroot_relativelinks; "

inherit gzipnative

LICENSE = "MIT"
PACKAGES = ""
DEPENDS += "${MLPREFIX}qemuwrapper-cross ${MLPREFIX}depmodwrapper-cross"
RDEPENDS += "${PACKAGE_INSTALL} ${LINGUAS_INSTALL}"
RRECOMMENDS += "${PACKAGE_INSTALL_ATTEMPTONLY}"

INHIBIT_DEFAULT_DEPS = "1"

TESTIMAGECLASS = "${@base_conditional('TEST_IMAGE', '1', 'testimage-auto', '', d)}"
inherit ${TESTIMAGECLASS}

# IMAGE_FEATURES may contain any available package group
IMAGE_FEATURES ?= ""
IMAGE_FEATURES[type] = "list"
IMAGE_FEATURES[validitems] += "debug-tweaks read-only-rootfs empty-root-password allow-empty-password post-install-logging"

# Generate companion debugfs?
IMAGE_GEN_DEBUGFS ?= "0"

# rootfs bootstrap install
ROOTFS_BOOTSTRAP_INSTALL = "${@bb.utils.contains("IMAGE_FEATURES", "package-management", "", "${ROOTFS_PKGMANAGE_BOOTSTRAP}",d)}"

# packages to install from features
FEATURE_INSTALL = "${@' '.join(oe.packagegroup.required_packages(oe.data.typed_value('IMAGE_FEATURES', d), d))}"
FEATURE_INSTALL[vardepvalue] = "${FEATURE_INSTALL}"
FEATURE_INSTALL_OPTIONAL = "${@' '.join(oe.packagegroup.optional_packages(oe.data.typed_value('IMAGE_FEATURES', d), d))}"
FEATURE_INSTALL_OPTIONAL[vardepvalue] = "${FEATURE_INSTALL_OPTIONAL}"

# Define some very basic feature package groups
FEATURE_PACKAGES_package-management = "${ROOTFS_PKGMANAGE}"
SPLASH ?= "psplash"
FEATURE_PACKAGES_splash = "${SPLASH}"

IMAGE_INSTALL_COMPLEMENTARY = '${@complementary_globs("IMAGE_FEATURES", d)}'

def check_image_features(d):
    valid_features = (d.getVarFlag('IMAGE_FEATURES', 'validitems', True) or "").split()
    valid_features += d.getVarFlags('COMPLEMENTARY_GLOB').keys()
    for var in d:
       if var.startswith("PACKAGE_GROUP_"):
           bb.warn("PACKAGE_GROUP is deprecated, please use FEATURE_PACKAGES instead")
           valid_features.append(var[14:])
       elif var.startswith("FEATURE_PACKAGES_"):
           valid_features.append(var[17:])
    valid_features.sort()

    features = set(oe.data.typed_value('IMAGE_FEATURES', d))
    for feature in features:
        if feature not in valid_features:
            if bb.utils.contains('EXTRA_IMAGE_FEATURES', feature, True, False, d):
                raise bb.parse.SkipRecipe("'%s' in IMAGE_FEATURES (added via EXTRA_IMAGE_FEATURES) is not a valid image feature. Valid features: %s" % (feature, ' '.join(valid_features)))
            else:
                raise bb.parse.SkipRecipe("'%s' in IMAGE_FEATURES is not a valid image feature. Valid features: %s" % (feature, ' '.join(valid_features)))

IMAGE_INSTALL ?= ""
IMAGE_INSTALL[type] = "list"
export PACKAGE_INSTALL ?= "${IMAGE_INSTALL} ${ROOTFS_BOOTSTRAP_INSTALL} ${FEATURE_INSTALL}"
PACKAGE_INSTALL_ATTEMPTONLY ?= "${FEATURE_INSTALL_OPTIONAL}"

# Images are generally built explicitly, do not need to be part of world.
EXCLUDE_FROM_WORLD = "1"

USE_DEVFS ?= "1"
USE_DEPMOD ?= "1"

PID = "${@os.getpid()}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LDCONFIGDEPEND ?= "ldconfig-native:do_populate_sysroot"
LDCONFIGDEPEND_libc-uclibc = ""
LDCONFIGDEPEND_libc-musl = ""

# This is needed to have depmod data in PKGDATA_DIR,
# but if you're building small initramfs image
# e.g. to include it in your kernel, you probably
# don't want this dependency, which is causing dependency loop
KERNELDEPMODDEPEND ?= "virtual/kernel:do_packagedata"

do_rootfs[depends] += " \
    makedevs-native:do_populate_sysroot virtual/fakeroot-native:do_populate_sysroot ${LDCONFIGDEPEND} \
    virtual/update-alternatives-native:do_populate_sysroot update-rc.d-native:do_populate_sysroot \
    ${KERNELDEPMODDEPEND} \
"
do_rootfs[recrdeptask] += "do_packagedata"

def command_variables(d):
    return ['ROOTFS_POSTPROCESS_COMMAND','ROOTFS_PREPROCESS_COMMAND','ROOTFS_POSTINSTALL_COMMAND','OPKG_PREPROCESS_COMMANDS','OPKG_POSTPROCESS_COMMANDS','IMAGE_POSTPROCESS_COMMAND',
            'IMAGE_PREPROCESS_COMMAND','ROOTFS_POSTPROCESS_COMMAND','POPULATE_SDK_POST_HOST_COMMAND','POPULATE_SDK_POST_TARGET_COMMAND','SDK_POSTPROCESS_COMMAND','RPM_PREPROCESS_COMMANDS',         
            'RPM_POSTPROCESS_COMMANDS']

python () {
    variables = command_variables(d)
    for var in variables:
        if d.getVar(var, False):
            d.setVarFlag(var, 'func', '1')
}

def fstype_variables(d):
    import oe.image

    image = oe.image.Image(d)
    alltypes, fstype_groups, cimages = image._get_image_types()
    fstype_vars = set()
    for fstype_group in fstype_groups:
        for fstype in fstype_group:
            fstype_vars.add('IMAGE_CMD_' + fstype)
            if fstype in cimages:
                for ctype in cimages[fstype]:
                    fstype_vars.add('COMPRESS_CMD_' + ctype)

    return sorted(fstype_vars)

def rootfs_variables(d):
    from oe.rootfs import variable_depends
    variables = ['IMAGE_DEVICE_TABLES','BUILD_IMAGES_FROM_FEEDS','IMAGE_TYPES_MASKED','IMAGE_ROOTFS_ALIGNMENT','IMAGE_OVERHEAD_FACTOR','IMAGE_ROOTFS_SIZE','IMAGE_ROOTFS_EXTRA_SPACE',
                 'IMAGE_ROOTFS_MAXSIZE','IMAGE_NAME','IMAGE_LINK_NAME','IMAGE_MANIFEST','DEPLOY_DIR_IMAGE','RM_OLD_IMAGE','IMAGE_FSTYPES','IMAGE_INSTALL_COMPLEMENTARY','IMAGE_LINGUAS','SDK_OS',
                 'SDK_OUTPUT','SDKPATHNATIVE','SDKTARGETSYSROOT','SDK_DIR','SDK_VENDOR','SDKIMAGE_INSTALL_COMPLEMENTARY','SDK_PACKAGE_ARCHS','SDK_OUTPUT','SDKTARGETSYSROOT','MULTILIBRE_ALLOW_REP',
                 'MULTILIB_TEMP_ROOTFS','MULTILIB_VARIANTS','MULTILIBS','ALL_MULTILIB_PACKAGE_ARCHS','MULTILIB_GLOBAL_VARIANTS','BAD_RECOMMENDATIONS','NO_RECOMMENDATIONS','PACKAGE_ARCHS',
                 'PACKAGE_CLASSES','TARGET_VENDOR','TARGET_VENDOR','TARGET_ARCH','TARGET_OS','OVERRIDES','BBEXTENDVARIANT','FEED_DEPLOYDIR_BASE_URI','INTERCEPT_DIR','USE_DEVFS',
                 'COMPRESSIONTYPES', 'IMAGE_GEN_DEBUGFS']
    variables.extend(fstype_variables(d))
    variables.extend(command_variables(d))
    variables.extend(variable_depends(d))
    return " ".join(variables)

do_rootfs[vardeps] += "${@rootfs_variables(d)}"

do_build[depends] += "virtual/kernel:do_deploy"

def build_live(d):
    if bb.utils.contains("IMAGE_FSTYPES", "live", "live", "0", d) == "0": # live is not set but hob might set iso or hddimg
        d.setVar('NOISO', bb.utils.contains('IMAGE_FSTYPES', "iso", "0", "1", d))
        d.setVar('NOHDD', bb.utils.contains('IMAGE_FSTYPES', "hddimg", "0", "1", d))
        if d.getVar('NOISO', True) == "0" or d.getVar('NOHDD', True) == "0":
            return "image-live"
        return ""
    return "image-live"

IMAGE_TYPE_live = "${@build_live(d)}"
inherit ${IMAGE_TYPE_live}

IMAGE_TYPE_vm = '${@bb.utils.contains_any("IMAGE_FSTYPES", ["vmdk", "vdi", "qcow2", "hdddirect"], "image-vm", "", d)}'
inherit ${IMAGE_TYPE_vm}

python () {
    deps = " " + imagetypes_getdepends(d)
    d.appendVarFlag('do_rootfs', 'depends', deps)

    deps = ""
    for dep in (d.getVar('EXTRA_IMAGEDEPENDS', True) or "").split():
        deps += " %s:do_populate_sysroot" % dep
    d.appendVarFlag('do_build', 'depends', deps)

    #process IMAGE_FEATURES, we must do this before runtime_mapping_rename
    #Check for replaces image features
    features = set(oe.data.typed_value('IMAGE_FEATURES', d))
    remain_features = features.copy()
    for feature in features:
        replaces = set((d.getVar("IMAGE_FEATURES_REPLACES_%s" % feature, True) or "").split())
        remain_features -= replaces

    #Check for conflict image features
    for feature in remain_features:
        conflicts = set((d.getVar("IMAGE_FEATURES_CONFLICTS_%s" % feature, True) or "").split())
        temp = conflicts & remain_features
        if temp:
            bb.fatal("%s contains conflicting IMAGE_FEATURES %s %s" % (d.getVar('PN', True), feature, ' '.join(list(temp))))

    d.setVar('IMAGE_FEATURES', ' '.join(list(remain_features)))

    check_image_features(d)
    initramfs_image = d.getVar('INITRAMFS_IMAGE', True) or ""
    if initramfs_image != "":
        d.appendVarFlag('do_build', 'depends', " %s:do_bundle_initramfs" %  d.getVar('PN', True))
        d.appendVarFlag('do_bundle_initramfs', 'depends', " %s:do_rootfs" % initramfs_image)
}

IMAGE_CLASSES += "image_types"
inherit ${IMAGE_CLASSES}

IMAGE_POSTPROCESS_COMMAND ?= ""

# some default locales
IMAGE_LINGUAS ?= "de-de fr-fr en-gb"

LINGUAS_INSTALL ?= "${@" ".join(map(lambda s: "locale-base-%s" % s, d.getVar('IMAGE_LINGUAS', True).split()))}"

# Prefer image, but use the fallback files for lookups if the image ones
# aren't yet available.
PSEUDO_PASSWD = "${IMAGE_ROOTFS}:${STAGING_DIR_NATIVE}"

inherit rootfs-postcommands

PACKAGE_EXCLUDE ??= ""
PACKAGE_EXCLUDE[type] = "list"

fakeroot python do_rootfs () {
    from oe.rootfs import create_rootfs
    from oe.image import create_image
    from oe.manifest import create_manifest

    # Handle package exclusions
    excl_pkgs = d.getVar("PACKAGE_EXCLUDE", True).split()
    inst_pkgs = d.getVar("PACKAGE_INSTALL", True).split()
    inst_attempt_pkgs = d.getVar("PACKAGE_INSTALL_ATTEMPTONLY", True).split()

    d.setVar('PACKAGE_INSTALL_ORIG', ' '.join(inst_pkgs))
    d.setVar('PACKAGE_INSTALL_ATTEMPTONLY', ' '.join(inst_attempt_pkgs))

    for pkg in excl_pkgs:
        if pkg in inst_pkgs:
            bb.warn("Package %s, set to be excluded, is in %s PACKAGE_INSTALL (%s).  It will be removed from the list." % (pkg, d.getVar('PN', True), inst_pkgs))
            inst_pkgs.remove(pkg)

        if pkg in inst_attempt_pkgs:
            bb.warn("Package %s, set to be excluded, is in %s PACKAGE_INSTALL_ATTEMPTONLY (%s).  It will be removed from the list." % (pkg, d.getVar('PN', True), inst_pkgs))
            inst_attempt_pkgs.remove(pkg)

    d.setVar("PACKAGE_INSTALL", ' '.join(inst_pkgs))
    d.setVar("PACKAGE_INSTALL_ATTEMPTONLY", ' '.join(inst_attempt_pkgs))

    # Ensure we handle package name remapping
    # We have to delay the runtime_mapping_rename until just before rootfs runs
    # otherwise, the multilib renaming could step in and squash any fixups that
    # may have occurred.
    pn = d.getVar('PN', True)
    runtime_mapping_rename("PACKAGE_INSTALL", pn, d)
    runtime_mapping_rename("PACKAGE_INSTALL_ATTEMPTONLY", pn, d)
    runtime_mapping_rename("BAD_RECOMMENDATIONS", pn, d)

    # Generate the initial manifest
    create_manifest(d)

    # Generate rootfs
    create_rootfs(d)

    # generate final images
    create_image(d)
}
do_rootfs[dirs] = "${TOPDIR}"
do_rootfs[cleandirs] += "${S}"
do_rootfs[umask] = "022"
addtask rootfs before do_build

MULTILIBRE_ALLOW_REP =. "${base_bindir}|${base_sbindir}|${bindir}|${sbindir}|${libexecdir}|${sysconfdir}|${nonarch_base_libdir}/udev|/lib/modules/[^/]*/modules.*|"
MULTILIB_CHECK_FILE = "${WORKDIR}/multilib_check.py"
MULTILIB_TEMP_ROOTFS = "${WORKDIR}/multilib"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"
do_package[noexec] = "1"
do_package_qa[noexec] = "1"
do_packagedata[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_package_write_deb[noexec] = "1"
do_package_write_rpm[noexec] = "1"

# Allow the kernel to be repacked with the initramfs and boot image file as a single file
do_bundle_initramfs[depends] += "virtual/kernel:do_bundle_initramfs"
do_bundle_initramfs[nostamp] = "1"
do_bundle_initramfs[noexec] = "1"
do_bundle_initramfs () {
	:
}
addtask bundle_initramfs after do_rootfs
