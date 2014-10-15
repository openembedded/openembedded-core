KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "6b57ab98e37cda3b4a4cd50483a43e053ae73d1b"
SRCREV_machine_qemumips ?= "80b24085b50144088cdfca737b5925a15ae9e312"
SRCREV_machine_qemuppc ?= "12a5e6b10d6d34878f26592160ff64668b021ab4"
SRCREV_machine_qemux86 ?= "0caf16d38536e3dec8a02ea657e1960f1216f174"
SRCREV_machine_qemux86-64 ?= "0caf16d38536e3dec8a02ea657e1960f1216f174"
SRCREV_machine_qemumips64 ?= "3b77b2d556103b86f787913385a7655b92adcc03"
SRCREV_machine ?= "0caf16d38536e3dec8a02ea657e1960f1216f174"
SRCREV_meta ?= "229ce533868773f201f9ab36e2b4248b381309ec"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.17.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.17.1"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
