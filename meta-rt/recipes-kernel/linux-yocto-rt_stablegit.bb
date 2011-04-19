inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE_qemux86-64  = "common_pc_64"
KMACHINE_atom-pc  = "atom-pc"

LINUX_VERSION ?= "2.6.34"
LINUX_KERNEL_TYPE = "preempt_rt"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE_EXTENSION}"

KMETA = wrs_meta
KBRANCH = ${KMACHINE}-${LINUX_KERNEL_TYPE}

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

COMPATIBLE_MACHINE = "(qemux86-64|atom-pc)"

# this performs a fixup on the SRCREV for new/undefined BSPs
python __anonymous () {
    import bb, re, string

    kerntype = string.replace(bb.data.expand("${LINUX_KERNEL_TYPE}", d), "_", "-")
    bb.data.setVar("LINUX_KERNEL_TYPE_EXTENSION", kerntype, d)
}

SRC_URI = "git://git.yoctoproject.org/linux-2.6-windriver.git;protocol=git;nocheckout=1;branch=${KBRANCH},wrs_meta;name=machine,meta"

# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES=features/netfilter

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout

require recipes-kernel/linux/linux-tools.inc
