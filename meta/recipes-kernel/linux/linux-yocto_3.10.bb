KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64  ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "896f87d1003331d7a307c9f855cdbb78c9a2a033"
SRCREV_machine_qemumips ?= "9e616945464ab97a050da96a025d88c809d05144"
SRCREV_machine_qemuppc ?= "692b2de590668de2e15461879cf9301a0e6fedf6"
SRCREV_machine_qemux86 ?= "747e1cbd12b15db8bc2ae86e2359c1b113f120d6"
SRCREV_machine_qemux86-64 ?= "747e1cbd12b15db8bc2ae86e2359c1b113f120d6"
SRCREV_machine_qemumips64 ?= "d237cab9f483ea512ded4ea311902763c1a3ae68"
SRCREV_machine ?= "747e1cbd12b15db8bc2ae86e2359c1b113f120d6"
SRCREV_meta ?= "8f05306a8e6f5ee422d50c3317acce0cf9e6aada"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.59"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
