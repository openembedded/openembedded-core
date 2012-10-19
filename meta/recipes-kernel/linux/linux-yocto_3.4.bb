require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "8ee53c3b82ada3cdfd7d25f07d3975834ac9a9b2"
SRCREV_machine_qemumips  ?= "caf99a20e3c3ba036ed1bb46875059a0d24e4fbd"
SRCREV_machine_qemuppc ?= "7833f1549c3d44cce8aea38b65cd501229aad492"
SRCREV_machine_qemux86 ?= "449f7f520350700858f21a5554b81cc8ad23267d"
SRCREV_machine_qemux86-64 ?= "449f7f520350700858f21a5554b81cc8ad23267d"
SRCREV_machine ?= "449f7f520350700858f21a5554b81cc8ad23267d"
SRCREV_meta ?= "0541ba5f98f31419e9bdfec895c18029bf424e7c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.11"

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
