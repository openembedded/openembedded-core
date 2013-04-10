require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "ec3299eaa89a7302009e3d46c9570f633ff32e9a"
SRCREV_machine_qemumips  ?= "e5e37d6aab5d82398f874f623ec9b37a8274f41a"
SRCREV_machine_qemuppc ?= "223428bbc1a355200bd9a8046fd272c1b9b13e67"
SRCREV_machine_qemux86 ?= "11998bd1f44b21cd0b8c0ca11cbd36865f14bfdc"
SRCREV_machine_qemux86-64 ?= "11998bd1f44b21cd0b8c0ca11cbd36865f14bfdc"
SRCREV_machine ?= "11998bd1f44b21cd0b8c0ca11cbd36865f14bfdc"
SRCREV_meta ?= "4c567e02266ebe383abc6755b4f7908696ac099d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8.4"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append_qemux86=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" ${KERNEL_EXTRA_FEATURES} cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
