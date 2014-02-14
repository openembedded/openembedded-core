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

SRCREV_machine_qemuarm ?= "51f493f9716a76fbfe911bfdefca7e9a42a5ca13"
SRCREV_machine_qemumips ?= "633b2257f21082fd3a784a4417b421e9cd008045"
SRCREV_machine_qemuppc ?= "37465d34ea1aba381082d22a880774b3b3b31af3"
SRCREV_machine_qemux86 ?= "3e0a296fae952d8d93eb0f96566bf6d4a978c8ee"
SRCREV_machine_qemux86-64 ?= "3e0a296fae952d8d93eb0f96566bf6d4a978c8ee"
SRCREV_machine_qemumips64 ?= "fe1fab7061b8369deca03b2ec59f186bdbe2b8d9"
SRCREV_machine ?= "3e0a296fae952d8d93eb0f96566bf6d4a978c8ee"
SRCREV_meta ?= "d57f7367a5f44a47b84425bb48601b41c6d0ed8a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.28"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
