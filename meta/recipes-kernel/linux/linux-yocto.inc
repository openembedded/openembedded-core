DESCRIPTION = "Yocto Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# A KMACHINE is the mapping of a yocto $MACHINE to what is built
# by the kernel. This is typically the branch that should be built,
# and it can be specific to the machine or shared
# KMACHINE = "UNDEFINED"

# Set this to 'preempt_rt' in the local.conf if you want a real time kernel
LINUX_KERNEL_TYPE ?= standard

KMETA ?= meta
SRCREV_FORMAT ?= "meta_machine" 

LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE}"
KERNEL_REVISION_CHECKING ?= "t"
YOCTO_KERNEL_META_DATA ?= t

do_patch[depends] = "kern-tools-native:do_populate_sysroot"

addtask kernel_configme before do_configure after do_patch

# Pick up shared functions
inherit kernel-yocto
require linux-dtb.inc

B = "${WORKDIR}/linux-${MACHINE}-${LINUX_KERNEL_TYPE}-build"

do_install_append(){
	if [ -n "${KMETA}" ]; then
		rm -rf ${STAGING_KERNEL_DIR}/${KMETA}
	fi
}

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout
addtask kernel_configcheck after do_configure before do_compile
