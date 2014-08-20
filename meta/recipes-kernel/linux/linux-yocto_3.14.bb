require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "c3aa4d4809315f226de525a20072d0055fbb2649"
SRCREV_machine_qemumips ?= "40fb9a61f54d3090be4046d0fef3ea635f53023e"
SRCREV_machine_qemuppc ?= "0fb630e8897fe7d4e8ddda667df5d5701b71c409"
SRCREV_machine_qemux86 ?= "6fc66b1dbd642850474145f49b387e19be1e04c9"
SRCREV_machine_qemux86-64 ?= "59ed057796efe35d264c2ebe4f79acdbe40026e5"
SRCREV_machine_qemumips64 ?= "886a5a31a12305148b7e7cac1200b521eec3fdfc"
SRCREV_machine ?= "59ed057796efe35d264c2ebe4f79acdbe40026e5"
SRCREV_meta ?= "3a897e59cb5de59e7fdaabdfa89db5da69966def"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.13"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
