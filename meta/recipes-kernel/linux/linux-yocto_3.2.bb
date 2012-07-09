require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/default/base"
KBRANCH_qemux86  = "standard/default/common-pc/base"
KBRANCH_qemux86-64  = "standard/default/common-pc-64/base"
KBRANCH_qemuppc  = "standard/default/qemu-ppc32"
KBRANCH_qemumips = "standard/default/mti-malta32-be"
KBRANCH_qemuarm  = "standard/default/arm-versatile-926ejs"

LINUX_VERSION ?= "3.2.18"

SRCREV_machine_qemuarm ?= "50a1849ed5e180dc900471d12f41e76f6fe38fff"
SRCREV_machine_qemumips  ?= "24a13cca4ace1cb16da0a6379514bdea82924c06"
SRCREV_machine_qemuppc ?= "b4c41eb3dfedb80c253886451bb4c5120c547c57"
SRCREV_machine_qemux86 ?= "0e2accb1b10421b7daac77144b11862a4bb47caa"
SRCREV_machine_qemux86-64 ?= "6c62f352ad9ca61b0cb3e0ff9ae474d64c02e6ef"
SRCREV_machine ?= "0ec416edf0b0cab3e919c0a1c167a883f8b344a2"
SRCREV_meta ?= "07ee09b520579b9f29bd15fefb01fd28b34c6064"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
