KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/base"
KBRANCH_qemux86-64 ?= "standard/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "89241b98386f752ab073d3ab5518cb69bacbd97e"
SRCREV_machine_qemuarm64 ?= "a38cb202738a2b055ac216b3699cc9377edea45a"
SRCREV_machine_qemumips ?= "e00505a0c07a352d1dd57adb8da1768863022979"
SRCREV_machine_qemuppc ?= "d5ef1ced9d019d20d986e205bddc317961407188"
SRCREV_machine_qemux86 ?= "a38cb202738a2b055ac216b3699cc9377edea45a"
SRCREV_machine_qemux86-64 ?= "a38cb202738a2b055ac216b3699cc9377edea45a"
SRCREV_machine_qemumips64 ?= "ac476ecd7a56288e3e8ed1ef3872554de661e823"
SRCREV_machine ?= "a38cb202738a2b055ac216b3699cc9377edea45a"
SRCREV_meta ?= "322fa5b2796bfcff7bfbbde1d76c73636ecf5857"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.33"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
