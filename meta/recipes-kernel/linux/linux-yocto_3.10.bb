require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "8675d3d0a3c17135f70ef474d5c362397c02e62a"
SRCREV_machine_qemumips  ?= "992b743b905edd21d3a4a27c92b5c6a9e44d7d73"
SRCREV_machine_qemuppc ?= "09388c3572e26c78b34554e46e077f12be7b2401"
SRCREV_machine_qemux86 ?= "6c1528b2b78d1ec7e75bb7a9880074ec35aa1aa0"
SRCREV_machine_qemux86-64 ?= "6c1528b2b78d1ec7e75bb7a9880074ec35aa1aa0"
SRCREV_machine_qemumips64 ?= "203679cb23c752fdcbc0180a55a81728d7462561"
SRCREV_machine ?= "6c1528b2b78d1ec7e75bb7a9880074ec35aa1aa0"
SRCREV_meta ?= "1b9b113837f353fd94148beb6bd00a3b7b35d01d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.9"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
