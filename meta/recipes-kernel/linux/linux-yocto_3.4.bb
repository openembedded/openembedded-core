require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "6db2c606429fa8671e76eb312cdd92f9451cf8e8"
SRCREV_machine_qemumips  ?= "a9f79fc1bde4f5adb7cb015d2f2b5a04bd5597a1"
SRCREV_machine_qemuppc ?= "492560e2f9a6864c5b4bbb24a35631c182fa35a5"
SRCREV_machine_qemux86 ?= "c77666c1d4c4be4be4b2046c3ff25bf1db34eb21"
SRCREV_machine_qemux86-64 ?= "c77666c1d4c4be4be4b2046c3ff25bf1db34eb21"
SRCREV_machine ?= "c77666c1d4c4be4be4b2046c3ff25bf1db34eb21"
SRCREV_meta ?= "28bcd46af1d592dab39bd8a0891c872454fde8bc"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.7"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
