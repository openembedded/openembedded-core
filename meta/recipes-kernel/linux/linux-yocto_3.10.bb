require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/base"
KBRANCH = "${KBRANCH_DEFAULT}"

SRCREV_machine_qemuarm ?= "d8c48090a1272c69e009c4ef5fa675fc8db178cc"
SRCREV_machine_qemumips  ?= "181188440e96896fbc69fa2c5ea5d2ee43304da0"
SRCREV_machine_qemuppc ?= "c0a23bc4b4d6be3d2f09a81dfeb3456f51b8d439"
SRCREV_machine_qemux86 ?= "b76f4452b72080a538406abc9b290a5d811d61f8"
SRCREV_machine_qemux86-64 ?= "b76f4452b72080a538406abc9b290a5d811d61f8"
SRCREV_machine_qemumips64 ?= "3a0d015e9908b69666c7f7780502aed4d115de1d"
SRCREV_machine ?= "b76f4452b72080a538406abc9b290a5d811d61f8"
SRCREV_meta ?= "cd502a88148ab214b54860f97a96f41858fd6446"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

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
