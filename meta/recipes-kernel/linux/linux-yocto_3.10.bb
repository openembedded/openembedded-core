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

SRCREV_machine_qemuarm ?= "dd18759fd19771885589066811e254852e56d421"
SRCREV_machine_qemumips ?= "2dbb607d161aeaa8d542b128203d7a10527aec7e"
SRCREV_machine_qemuppc ?= "92dda91b216cbacfa71fd9036413dba0d2406332"
SRCREV_machine_qemux86 ?= "02f7e63e56c061617957388c23bd5cf9b05c5388"
SRCREV_machine_qemux86-64 ?= "02f7e63e56c061617957388c23bd5cf9b05c5388"
SRCREV_machine_qemumips64 ?= "ebd521c1b609b445d8baf522d71453d4c26a0440"
SRCREV_machine ?= "02f7e63e56c061617957388c23bd5cf9b05c5388"
SRCREV_meta ?= "1eb79862c2959cfa305773e7f785d6d1810bf190"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.38"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
