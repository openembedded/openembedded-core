KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "c32a2e1190325f685a61196f38fc03ceff2645bf"
SRCREV_machine_qemumips ?= "1ff50c529b49ceb55a670258f7cb8233c8006c6c"
SRCREV_machine_qemuppc ?= "12e9828a5b78891374375233b4075f682b9fbd0e"
SRCREV_machine_qemux86 ?= "58d49fe770ad642e2bc0616159195ba837dc513b"
SRCREV_machine_qemux86-64 ?= "b85edae6fd61ceadfc08099608e8ac90aa4c5c33"
SRCREV_machine_qemumips64 ?= "dcd579737386fca4d7bcf224bc118b14eb3c1d55"
SRCREV_machine ?= "b85edae6fd61ceadfc08099608e8ac90aa4c5c33"
SRCREV_meta ?= "a94680bfeb0e8e3025b57f8b42d0825ba743376b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.17"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
