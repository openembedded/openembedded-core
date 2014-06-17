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

SRCREV_machine_qemuarm ?= "192b56b2f529af1be014ea85667c9f3fea0afd53"
SRCREV_machine_qemumips  ?= "58fb8b8fb2f9911cac84cd840d63c8a58bada6ca"
SRCREV_machine_qemuppc ?= "4fe50989bc8bcb3564ca37c2cffd42ac176b428d"
SRCREV_machine_qemux86 ?= "498189ccb98f833daa2092ceee72da8c878e0009"
SRCREV_machine_qemux86-64 ?= "498189ccb98f833daa2092ceee72da8c878e0009"
SRCREV_machine ?= "498189ccb98f833daa2092ceee72da8c878e0009"
SRCREV_meta ?= "a8742041c8b9f447d4ad4c3f478e022f1e4bfcfd"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.4.91"

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
