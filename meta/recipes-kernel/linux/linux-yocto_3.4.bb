require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "2ed05c280920e592f62d09bda15ab006baffa996"
SRCREV_machine_qemumips  ?= "81ef4ad8f02c979a236a460c6831dcb4fbf2ef3c"
SRCREV_machine_qemuppc ?= "6b5d5a60ca6c3cf05d28c9ad447c133563340232"
SRCREV_machine_qemux86 ?= "7146d001a5f95068a3e2da23a8b3d15aeb20087a"
SRCREV_machine_qemux86-64 ?= "7146d001a5f95068a3e2da23a8b3d15aeb20087a"
SRCREV_machine ?= "7146d001a5f95068a3e2da23a8b3d15aeb20087a"
SRCREV_meta ?= "2cbb5aca2c25e37b04828cf0b7d90e2f22af816e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.24"

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
