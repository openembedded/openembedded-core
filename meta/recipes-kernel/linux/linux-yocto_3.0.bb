inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.9"

SRCREV_machine_qemuarm ?= "774710abff369c063226452de2114403e0bbff9f"
SRCREV_machine_qemumips ?= "a558a07a706756cd83199146c89c2677d9f14364"
SRCREV_machine_qemuppc ?= "d06082c227f12f62b9ce9e3823f8b7d2ad055880"
SRCREV_machine_qemux86 ?= "a231348953ce9f1bc2f520b2fe6806337efccfa8"
SRCREV_machine_qemux86-64 ?= "08db6e4d159a26a4caf06258b1d7dbd17e6ebe26"
SRCREV_machine ?= "5c974770213858c380599a0fb743f24ffb619d62"
SRCREV_meta ?= "74665003a89510fb5bd4e29e81d568d72eede08c"

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
