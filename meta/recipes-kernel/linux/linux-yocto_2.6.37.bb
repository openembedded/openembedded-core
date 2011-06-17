inherit kernel
require linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}
KMETA = meta

LINUX_VERSION ?= "2.6.37"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE}"

SRCREV_machine_qemuarm = "52424d35025cfa43f0d66cf6fa982a5c289a1ba4"
SRCREV_machine_qemumips = "9083f18f40fc6a2baf76fa001bc4858e02f40378"
SRCREV_machine_qemuppc = "9d278962ff228ea9627a85b0d21ed717246ebf1f"
SRCREV_machine_qemux86 = "57545699dff0b384bb16be74b0358059da5cce6a"
SRCREV_machine_qemux86-64 = "5d36c23eecdc9663c9c3a2a511d4872d3b3fd669"
SRCREV_machine = "12646d158aced2ba0122787bd56ff554e371091c"
SRCREV_meta = "8888d948e0ddbc5f37bd3dc23cc1b398ab230e2e"

PR = "r18"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-2.6.37;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

YOCTO_KERNEL_META_DATA=t

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout
addtask kernel_configcheck after do_configure before do_compile

require linux-tools.inc
