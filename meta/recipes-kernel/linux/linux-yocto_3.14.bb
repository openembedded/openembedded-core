require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"

# board specific branches
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemumips64 = "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "cb29a7b2d69ec89d851c9ad5a44b69c62feb7963"
SRCREV_machine_qemumips ?= "b9a7220e0dbe4faac06bedff201102d1642e32d1"
SRCREV_machine_qemuppc ?= "9ae4c4cdc2737f2570b7f71314be68c065179b53"
SRCREV_machine_qemux86 ?= "5542d96bf43c6c9f7c81cf36259e03d21ed5c210"
SRCREV_machine_qemux86-64 ?= "6cfe9c448741ab317d70a8ded3a953a0e66bb0a5"
SRCREV_machine_qemumips64 ?= "a779a38ca4a1cf78c6f07071ff51937214ff063d"
SRCREV_machine ?= "6cfe9c448741ab317d70a8ded3a953a0e66bb0a5"
SRCREV_meta ?= "3eefa4379f073768df150184e9dad1ff3228a0ff"

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
