require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "ccc8f93d42a56511b8867ff3eeba76290309bc2c"
SRCREV_machine_qemumips  ?= "23607b7f3ca690d62a43c58b8358fa5b75c55b2a"
SRCREV_machine_qemuppc ?= "d48004092883cfdee8a5092b375ac635742c39cb"
SRCREV_machine_qemux86 ?= "778950f1e0b5c5af7e92c43220c3c4f4e3324cb5"
SRCREV_machine_qemux86-64 ?= "778950f1e0b5c5af7e92c43220c3c4f4e3324cb5"
SRCREV_machine ?= "778950f1e0b5c5af7e92c43220c3c4f4e3324cb5"
SRCREV_meta ?= "9473a39c59bf9c07a316486d272652bacb9ad3ac"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.46"

PR = "${INC_PR}.5"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
