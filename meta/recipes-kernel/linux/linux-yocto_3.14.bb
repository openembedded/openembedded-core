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

SRCREV_machine_qemuarm ?= "38d0249598bef8263f1f1e280badf6b92a0bc668"
SRCREV_machine_qemuarm64 ?= "3bab81113682d3a5ffc8ea60cf770beed4831492"
SRCREV_machine_qemumips ?= "d2138247c6f1ba8f082f7c2b0d4a6a4efb0cf908"
SRCREV_machine_qemuppc ?= "2ec2927314d58a012403cbaccdf0a8a1f5c5d666"
SRCREV_machine_qemux86 ?= "9ed9a63eeafbcbee0e378e304a1029bb14d45697"
SRCREV_machine_qemux86-64 ?= "3bab81113682d3a5ffc8ea60cf770beed4831492"
SRCREV_machine_qemumips64 ?= "afdaa94d540bf671f4d4d198ec6b891df22fb323"
SRCREV_machine ?= "3bab81113682d3a5ffc8ea60cf770beed4831492"
SRCREV_meta ?= "6eddbf47875ef48ddc5864957a7b63363100782b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.14.29"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
