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

SRCREV_machine_qemuarm ?= "e3163012f3e09fe48374ef22bc676f8b19aeec1a"
SRCREV_machine_qemumips ?= "01a71aaf8e545c3ef88da1fe02f53d1b96a2640e"
SRCREV_machine_qemuppc ?= "ae1b9115975ff235038d5da22b4cc984b4d76aae"
SRCREV_machine_qemux86 ?= "79af968f2f26378798aec7a6d729ff5a371aae5f"
SRCREV_machine_qemux86-64 ?= "79af968f2f26378798aec7a6d729ff5a371aae5f"
SRCREV_machine_qemumips64 ?= "67efb2993ec7726df5567e2572fd64e34e29b46d"
SRCREV_machine ?= "79af968f2f26378798aec7a6d729ff5a371aae5f"
SRCREV_meta ?= "778d5f6259f0b8e28a46d8a764979e20e5a8ffc4"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},${KMETA};name=machine,meta"

LINUX_VERSION ?= "3.10.25"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
