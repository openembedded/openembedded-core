inherit module_strip

inherit kernel-arch

export OS = "${TARGET_OS}"
export CROSS_COMPILE = "${TARGET_PREFIX}"

export KERNEL_VERSION = "${@base_read_file('${STAGING_KERNEL_DIR}/kernel-abiversion')}"
KERNEL_OBJECT_SUFFIX = ".ko"

# kernel modules are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

#
# Ensure the hostprogs are available for module compilation. Modules that
# inherit this recipe and override do_compile() should be sure to call
# do_make_scripts() or ensure the scripts are built independently.
#
do_make_scripts() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS 
	make CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${KERNEL_AR}" \
	           -C ${STAGING_KERNEL_DIR} scripts
}
