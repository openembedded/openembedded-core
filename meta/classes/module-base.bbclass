inherit kernel-arch

export OS = "${TARGET_OS}"
export CROSS_COMPILE = "${TARGET_PREFIX}"

export KERNEL_VERSION = "${@base_read_file('${STAGING_KERNEL_DIR}/kernel-abiversion')}"
KERNEL_OBJECT_SUFFIX = ".ko"

# kernel modules are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Function to ensure the kernel scripts are created. Expected to
# be called before do_compile. See module.bbclass for an exmaple.
do_make_scripts() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS 
	make CC="${KERNEL_CC}" LD="${KERNEL_LD}" AR="${KERNEL_AR}" \
	           -C ${STAGING_KERNEL_DIR} scripts
}
