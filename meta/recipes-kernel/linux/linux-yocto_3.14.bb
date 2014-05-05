require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "54ecc26cb84e939753eee811395435f8a4377c86"
SRCREV_machine_qemumips ?= "cc7b8670680fcd794dba0b21a1db1dbbda432924"
SRCREV_machine_qemuppc ?= "5ac4b714c656323b6099e8ab435fc05903d69874"
SRCREV_machine_qemux86 ?= "3904476fb890e014a244e1fc48342683d7ff7e0e"
SRCREV_machine_qemux86-64 ?= "b0b9c962ea01f9356fc1542b9696ebe4a38e196a"
SRCREV_machine_qemumips64 ?= "a9c251d64f5df58c41473ccb1d52143cc5247911"
SRCREV_machine ?= "b0b9c962ea01f9356fc1542b9696ebe4a38e196a"
SRCREV_meta ?= "4df1e2ed992adeac4da60ad5118d0237e8cb88df"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.2"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
