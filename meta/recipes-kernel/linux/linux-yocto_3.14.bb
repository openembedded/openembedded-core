KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "4af8557eb8576ba957980705a3719aca0e27836e"
SRCREV_machine_qemuarm64 ?= "1e0bbd1dd68e255a9af7c36930debc6dd4885d0c"
SRCREV_machine_qemumips ?= "520d5d89434c61b4a95ac0c68d8e74fc8068e35b"
SRCREV_machine_qemuppc ?= "73e4399fd8db810cb59423767f9ca82a54311c7b"
SRCREV_machine_qemux86 ?= "19e871b55f80d4cbd406b64d3d5fabf6103e6f1c"
SRCREV_machine_qemux86-64 ?= "1e0bbd1dd68e255a9af7c36930debc6dd4885d0c"
SRCREV_machine_qemumips64 ?= "6a469761ad8b3a0e585e53b842ea8d55d05089e3"
SRCREV_machine ?= "1e0bbd1dd68e255a9af7c36930debc6dd4885d0c"
SRCREV_meta ?= "6eddbf47875ef48ddc5864957a7b63363100782b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.29"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
