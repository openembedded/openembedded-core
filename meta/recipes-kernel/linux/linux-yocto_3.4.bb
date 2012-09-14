require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "679978b4fd1ef8cfea97d4ee4b4cd7c51bc62ee2"
SRCREV_machine_qemumips  ?= "9c7810609ff37a77e8d39680f98f1baefee18a80"
SRCREV_machine_qemuppc ?= "cf4bad14983753ad4b592c40fb36466b202b24a4"
SRCREV_machine_qemux86 ?= "59c3ff750831338d05ab67d5efd7fc101c451aff"
SRCREV_machine_qemux86-64 ?= "59c3ff750831338d05ab67d5efd7fc101c451aff"
SRCREV_machine ?= "59c3ff750831338d05ab67d5efd7fc101c451aff"
SRCREV_meta ?= "7c50e572635d356f0b66d9ab90823f127f835744"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.10"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES_append = " features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32", "" ,d)}"
