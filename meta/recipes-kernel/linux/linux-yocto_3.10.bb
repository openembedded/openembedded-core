require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "df5844fa448e31927884d1850cfcc9bea981165c"
SRCREV_machine_qemumips ?= "4ffaa6116722ced25c4f775990f445c353d5b8e9"
SRCREV_machine_qemuppc ?= "db7c6be000d0a07d8b31561753c83ccbb4ae41ab"
SRCREV_machine_qemux86 ?= "a86e2b1eadd1f607d0d6ac5c4ab20a902714ddb1"
SRCREV_machine_qemux86-64 ?= "a86e2b1eadd1f607d0d6ac5c4ab20a902714ddb1"
SRCREV_machine_qemumips64 ?= "fe0da5cbc516f7686af316cdeafaf7b7e8d34d7d"
SRCREV_machine ?= "a86e2b1eadd1f607d0d6ac5c4ab20a902714ddb1"
SRCREV_meta ?= "7b3b87d4d5e4c41c235da13aaa9f45d5d338e2c6"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.32"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
