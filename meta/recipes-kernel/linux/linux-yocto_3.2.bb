require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/default/base"
KBRANCH_qemux86  = "standard/default/common-pc/base"
KBRANCH_qemux86-64  = "standard/default/common-pc-64/base"
KBRANCH_qemuppc  = "standard/default/qemu-ppc32"
KBRANCH_qemumips = "standard/default/mti-malta32-be"
KBRANCH_qemuarm  = "standard/default/arm-versatile-926ejs"

LINUX_VERSION ?= "3.2.32"

SRCREV_machine_qemuarm ?= "ec68698f59bed87be4e13954d5d2b9dc1f314b45"
SRCREV_machine_qemumips  ?= "ce21a2ab86277e410fd2229a10c45be6c9d0aa17"
SRCREV_machine_qemuppc ?= "edd27681039f2077d83eb02d7c1c44afb7b6224f"
SRCREV_machine_qemux86 ?= "2223d2fb00365c168ec380bdcbef05ef89751c85"
SRCREV_machine_qemux86-64 ?= "2d7956d7e3d15f69c923315c175847813f353d39"
SRCREV_machine ?= "6970a8f4f7caa2633aa1ae0b51732b246eb581ef"
SRCREV_meta ?= "e7f2fdc48f8808887175f0328274a2668084738c"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES_append=" features/netfilter/netfilter.scc"
KERNEL_FEATURES_append=" features/taskstats/taskstats.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
