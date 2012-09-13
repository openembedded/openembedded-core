require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "b15e7b1e9b58b9863bd87778775f86cd8d8880ea"
SRCREV_machine_qemumips  ?= "8d5b98f263b5119af2dc30223f311be17173bab9"
SRCREV_machine_qemuppc ?= "b9a720ca38d298ed457f37d099c85771f9164b19"
SRCREV_machine_qemux86 ?= "46d8c757b3be1953f30d6745505d24436e2d6844"
SRCREV_machine_qemux86-64 ?= "46d8c757b3be1953f30d6745505d24436e2d6844"
SRCREV_machine ?= "46d8c757b3be1953f30d6745505d24436e2d6844"
SRCREV_meta ?= "79947f1eb9f695c374ba63672f94deaa1de75561"

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
