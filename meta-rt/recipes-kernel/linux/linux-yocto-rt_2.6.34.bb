inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE_qemux86  = "common_pc"
KMACHINE_qemux86-64  = "common_pc_64"
KMACHINE_atom-pc  = "atom-pc"

LINUX_VERSION ?= "2.6.34"
LINUX_KERNEL_TYPE = "preempt_rt"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE_EXTENSION}"

KMETA = wrs_meta
KBRANCH = ${KMACHINE}-${LINUX_KERNEL_TYPE}

SRCREV_machine_qemux86 = "439602eb6acd53d9beb8493710310214fc7bd749"
SRCREV_machine_qemux86-64 = "3c84c45ad3c3592f9c7ff4076de9bee417cd322e"
SRCREV_machine_atom-pc = "269d71029adcf4d1dbf8441f091d824478d8c87d"
SRCREV_meta = "e1f85a470934a0cf6abde5d95533e74501822c6b"

PR = "r2"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

COMPATIBLE_MACHINE = "(qemux86|qemux86-64|atom-pc)"

# this performs a fixup on the SRCREV for new/undefined BSPs
python __anonymous () {
    import bb, re, string

    kerntype = string.replace(bb.data.expand("${LINUX_KERNEL_TYPE}", d), "_", "-")
    bb.data.setVar("LINUX_KERNEL_TYPE_EXTENSION", kerntype, d)
}

SRC_URI = "git://git.yoctoproject.org/linux-yocto-2.6.34.git;protocol=git;nocheckout=1;branch=${KBRANCH},wrs_meta;name=machine,meta"

# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES=features/netfilter

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout

require recipes-kernel/linux/linux-tools.inc
