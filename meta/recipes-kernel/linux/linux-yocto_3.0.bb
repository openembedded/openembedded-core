inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.14"


SRCREV_machine_qemuarm ?= "c0386e4195c685b0ff94b7e21581ec47008ab13b"
SRCREV_machine_qemumips ?= "56e4361a1bfc10042d83a7d8a5f96932056e3f35"
SRCREV_machine_qemuppc ?= "53e71af30e30162e17e32b2f57e4481f5bc0573e"
SRCREV_machine_qemux86 ?= "9e73573b6b227ab2c62e66e63470866d136f8d76"
SRCREV_machine_qemux86-64 ?= "3551fd0412965b1f6b0b2f6ba35f7fecdaddb58a"
SRCREV_machine ?= "6f5b11830deba4a6262c8c4abf67e608924f649e"
SRCREV_meta ?= "73dafd44ea875df654129b32b2877f342d5573e4"

PR = "r2"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
