require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "8fb1a478c9a05362e2e4e62fc30f5ef5d6c21f49"
SRCREV_machine_qemumips  ?= "b8870f2b11f4c948ae90a19886335fa8b7fca487"
SRCREV_machine_qemuppc ?= "e4c12f12e61a29b6605c4fcbcfd6dbe18bd7b4e4"
SRCREV_machine_qemux86 ?= "dd089cb5ba37ea14e8f90a884bf2a5be64ed817d"
SRCREV_machine_qemux86-64 ?= "dd089cb5ba37ea14e8f90a884bf2a5be64ed817d"
SRCREV_machine ?= "dd089cb5ba37ea14e8f90a884bf2a5be64ed817d"
SRCREV_meta ?= "8482dcdf68f9f7501118f4c01fdcb8f851882997"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.8.git;protocol=git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.8.11"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
