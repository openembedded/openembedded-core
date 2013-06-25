require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "7054a5db2c1869e0e0b294459fc4770113a8df52"
SRCREV_machine_qemumips  ?= "46b3cf22c01e40ef035b78f0542a9007aa0cf507"
SRCREV_machine_qemuppc ?= "1f2475ab9eefbb479c8a481475ddb3043d47b74a"
SRCREV_machine_qemux86 ?= "de0c0ed674dfdbd808657e299fc720d8a97cb868"
SRCREV_machine_qemux86-64 ?= "de0c0ed674dfdbd808657e299fc720d8a97cb868"
SRCREV_machine ?= "de0c0ed674dfdbd808657e299fc720d8a97cb868"
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
