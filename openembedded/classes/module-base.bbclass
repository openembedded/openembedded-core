inherit module_strip

inherit kernel-arch

export OS = "${TARGET_OS}"
export CROSS_COMPILE = "${TARGET_PREFIX}"

export KERNEL_VERSION = "${@base_read_file('${STAGING_KERNEL_DIR}/kernel-abiversion')}"
export KERNEL_SOURCE = "${@base_read_file('${STAGING_KERNEL_DIR}/kernel-source')}"
KERNEL_OBJECT_SUFFIX = "${@[".o", ".ko"][base_read_file('${STAGING_KERNEL_DIR}/kernel-abiversion') > "2.6.0"]}"
KERNEL_CCSUFFIX = "${@base_read_file('${STAGING_KERNEL_DIR}/kernel-ccsuffix')}"
KERNEL_LDSUFFIX = "${@base_read_file('${STAGING_KERNEL_DIR}/kernel-ldsuffix')}"

# Set TARGET_??_KERNEL_ARCH in the machine .conf to set architecture
# specific options necessary for building the kernel and modules.
TARGET_CC_KERNEL_ARCH ?= ""
HOST_CC_KERNEL_ARCH ?= "${TARGET_CC_KERNEL_ARCH}"
TARGET_LD_KERNEL_ARCH ?= ""
HOST_LD_KERNEL_ARCH ?= "${TARGET_LD_KERNEL_ARCH}"

KERNEL_CC = "${CCACHE}${HOST_PREFIX}gcc${KERNEL_CCSUFFIX} ${HOST_CC_KERNEL_ARCH}"
KERNEL_LD = "${LD}${KERNEL_LDSUFFIX} ${HOST_LD_KERNEL_ARCH}"

# kernel modules are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"
