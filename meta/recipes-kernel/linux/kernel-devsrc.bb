SUMMARY = "Linux kernel Development Source"
DESCRIPTION = "Development source linux kernel. When built, this recipe packages the \
source of the preferred virtual/kernel provider and makes it available for full kernel \
development or external module builds"

SECTION = "kernel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit linux-kernel-base

# Whilst not a module, this ensures we don't get multilib extended (which would make no sense)
inherit module-base

# We need the kernel to be staged (unpacked, patched and configured) before
# we can grab the source and make the kernel-devsrc package
do_install[depends] += "virtual/kernel:do_populate_sysroot"

# There's nothing to do here, except install the source where we can package it
do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_populate_sysroot[noexec] = "1"

# Define where the kernel headers are installed on the target as well as where
# they are staged.
KERNEL_SRC_PATH = "/usr/src/kernel"
S = "${STAGING_DIR_TARGET}/${KERNEL_SRC_PATH}"

KERNEL_VERSION = "${@get_kernelversion_headers('${S}')}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
        kerneldir=${D}${KERNEL_SRC_PATH}
        install -d $kerneldir

        #
        # Copy the staging dir source (and module build support) into the devsrc structure.
        # We can keep this copy simple and take everything, since a we'll clean up any build
        # artifacts afterwards, and the extra i/o is not significant
        #
        cd ${S}
        find . -type d -name '.git*' -prune -o -type f -print0 | cpio --null -pdlu $kerneldir
        oe_runmake -C $kerneldir CC="${KERNEL_CC}" LD="${KERNEL_LD}" clean _mrproper_scripts

        # As of Linux kernel version 3.0.1, the clean target removes
        # arch/powerpc/lib/crtsavres.o which is present in
        # KBUILD_LDFLAGS_MODULE, making it required to build external modules.
        if [ ${ARCH} = "powerpc" ]; then
                mkdir -p $kerneldir/arch/powerpc/lib/
                cp ${S}/arch/powerpc/lib/crtsavres.o $kerneldir/arch/powerpc/lib/crtsavres.o
        fi
}
# Ensure we don't race against "make scripts" during cpio
do_install[lockfiles] = "${TMPDIR}/kernel-scripts.lock"

PACKAGES = "kernel-devsrc"
FILES_${PN} = "${KERNEL_SRC_PATH}"
RDEPENDS_${PN} = "bc"
