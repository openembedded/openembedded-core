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

SRCREV_machine_qemuarm ?= "a3a7014ff1104ce5f82da9c4d50fa2ec792e1463"
SRCREV_machine_qemumips ?= "20f36e63a0e2de14c68e3031058fe6c7764a1926"
SRCREV_machine_qemuppc ?= "9d34a73cf18c3bed1e1bc2df6d1e3ccedec87c8a"
SRCREV_machine_qemux86 ?= "f53a6114b3a6e8c03ca4752de829887015f4c942"
SRCREV_machine_qemux86-64 ?= "f53a6114b3a6e8c03ca4752de829887015f4c942"
SRCREV_machine_qemumips64 ?= "093261d8a9879ac056d4f261545658addc71e452"
SRCREV_machine ?= "f53a6114b3a6e8c03ca4752de829887015f4c942"
SRCREV_meta ?= "13ae75f4a26fcd65abe526ef5f1dcde0583f453d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.40"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
