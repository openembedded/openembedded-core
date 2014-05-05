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

SRCREV_machine_qemuarm ?= "2ed52bacbd346dac90f65138daf16c17b2fd5066"
SRCREV_machine_qemumips  ?= "3e67c74737d97909d7ec069579fde35419a04f00"
SRCREV_machine_qemuppc ?= "fe300607aba102ec9f567eb966ed067645966e7b"
SRCREV_machine_qemux86 ?= "2ab0a0cc26d2fc3e59f66a27e8a6de8bd608a2d5"
SRCREV_machine_qemux86-64 ?= "2ab0a0cc26d2fc3e59f66a27e8a6de8bd608a2d5"
SRCREV_machine ?= "2ab0a0cc26d2fc3e59f66a27e8a6de8bd608a2d5"
SRCREV_meta ?= "03cd6f841fd2566b033db19eb6665228ea406adf"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.88"

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
