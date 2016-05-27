KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/base"
KBRANCH_qemux86-64 ?= "standard/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "b74c7523581ab748dfe475af384fcb40c4542d31"
SRCREV_machine_qemuarm64 ?= "53e84104c5e68eb468823dd0d262a64623d01a55"
SRCREV_machine_qemumips ?= "f5f23899a4afb7f095a480652edc082d6bd545b2"
SRCREV_machine_qemuppc ?= "53e84104c5e68eb468823dd0d262a64623d01a55"
SRCREV_machine_qemux86 ?= "53e84104c5e68eb468823dd0d262a64623d01a55"
SRCREV_machine_qemux86-64 ?= "53e84104c5e68eb468823dd0d262a64623d01a55"
SRCREV_machine_qemumips64 ?= "7ce6e58742b2f1f79cc8b1a1fff8bc408a3b585e"
SRCREV_machine ?= "53e84104c5e68eb468823dd0d262a64623d01a55"
SRCREV_meta ?= "3a5f494784591e01f0ae7ab748e8190582c16e8c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.11"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
