require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "b5a80c6a46604d7636c18a3e1ad362d1e6ed234e"
SRCREV_machine_qemumips  ?= "66bc662b2cd469a9807fbe838069d3f85cb01bd9"
SRCREV_machine_qemuppc ?= "d771422df10af41ee61598f4aa06e3c3a018c323"
SRCREV_machine_qemux86 ?= "59c2a9eb334c2def405c9d93ed6d8d4e822d1945"
SRCREV_machine_qemux86-64 ?= "59c2a9eb334c2def405c9d93ed6d8d4e822d1945"
SRCREV_machine ?= "59c2a9eb334c2def405c9d93ed6d8d4e822d1945"
SRCREV_meta ?= "c0b3904d60830e24b3930b0fa606a48b2758d979"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.26"

PR = "${INC_PR}.3"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES_append = " features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
KERNEL_FEATURES_append_qemux86=" cfg/paravirt_kvm"
#KERNEL_FEATURES_append_qemux86-64=" cfg/paravirt_kvm"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32", "" ,d)}"
