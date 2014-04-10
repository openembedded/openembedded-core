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

SRCREV_machine_qemuarm ?= "7b466aae62395490769f96635bd077ee8f313f18"
SRCREV_machine_qemumips ?= "cc5059ae360e0586375bbd1ed46e1e6f3e267dea"
SRCREV_machine_qemuppc ?= "663a3068c9756137a84bbfb3a76eeae7cb4373e1"
SRCREV_machine_qemux86 ?= "cee957655fe67826b2e827e2db41f156fa8f0cc4"
SRCREV_machine_qemux86-64 ?= "cee957655fe67826b2e827e2db41f156fa8f0cc4"
SRCREV_machine_qemumips64 ?= "a1491c3b367a75c7003b2fb58706e3631413b337"
SRCREV_machine ?= "cee957655fe67826b2e827e2db41f156fa8f0cc4"
SRCREV_meta ?= "7df9ef8ee47dc9023044614210f4c1d9d916dd5f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.35"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
