KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "3d5be74be2cab047a1b85e07620beee4791d4594"
SRCREV_machine_qemumips ?= "2834013809154887772cf1552191a682ed42d9eb"
SRCREV_machine_qemuppc ?= "44dddef534a3cae1aa952f77f4fd299978552307"
SRCREV_machine_qemux86 ?= "d77fb23dcc717061f74678babaeb4fa487c55fec"
SRCREV_machine_qemux86-64 ?= "fabce749eb25b8053250b5507868a246ddaf4423"
SRCREV_machine_qemumips64 ?= "5c5d8fd23d2b8fb0e8e00e54d2783feeba86ebd0"
SRCREV_machine ?= "fabce749eb25b8053250b5507868a246ddaf4423"
SRCREV_meta ?= "80c78725e098d26b9464c4d5cffd37c28f42dc7b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.18"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
