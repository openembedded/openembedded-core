require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "ca1bbde92c0d66ba8db6ac4654de12c0fbd5c0c8"
SRCREV_machine_qemumips  ?= "c349c5fd5d9dc04b3983565c239805f2c30ce6ca"
SRCREV_machine_qemuppc ?= "475a5e7d76f4e2914caefda3fed0adf40d0a2cc6"
SRCREV_machine_qemux86 ?= "0d734af0fa4dbef33cb486e147dccf35c4ad0900"
SRCREV_machine_qemux86-64 ?= "0d734af0fa4dbef33cb486e147dccf35c4ad0900"
SRCREV_machine ?= "0d734af0fa4dbef33cb486e147dccf35c4ad0900"
SRCREV_meta ?= "7c258779acffc9be33c7a31fa799dd416e9784a0"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.16"

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
KERNEL_FEATURES_append_qemux86-64=" cfg/paravirt_kvm"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32", "" ,d)}"
