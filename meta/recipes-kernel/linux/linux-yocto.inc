DESCRIPTION = "Yocto Kernel"
SECTION = "kernel"
LICENSE = "GPL"

# A KMACHINE is the mapping of a yocto $MACHINE to what is built
# by the kernel. This is typically the branch that should be built,
# and it can be specific to the machine or shared
KMACHINE = "UNDEFINED"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64|atom-pc|routerstationpro)"

# Set this to 'preempt_rt' in the local.conf if you want a real time kernel
LINUX_KERNEL_TYPE ?= standard

do_patch[depends] = "kern-tools-native:do_populate_sysroot"

addtask kernel_configme before do_configure after do_patch
addtask kernel_configcheck after do_configure before do_compile

# Pick up shared functions
inherit kernel-yocto

B = "${WORKDIR}/linux-${KMACHINE}-${LINUX_KERNEL_TYPE}-build"
