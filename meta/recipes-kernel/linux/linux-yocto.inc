DESCRIPTION = "Yocto Kernel"
SECTION = "kernel"
LICENSE = "GPL"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# A KMACHINE is the mapping of a yocto $MACHINE to what is built
# by the kernel. This is typically the branch that should be built,
# and it can be specific to the machine or shared
# KMACHINE = "UNDEFINED"

# Set this to 'preempt_rt' in the local.conf if you want a real time kernel
LINUX_KERNEL_TYPE ?= standard

do_patch[depends] = "kern-tools-native:do_populate_sysroot"

addtask kernel_configme before do_configure after do_patch

# Pick up shared functions
inherit kernel-yocto
require linux-dtb.inc

B = "${WORKDIR}/linux-${MACHINE}-${LINUX_KERNEL_TYPE}-build"
