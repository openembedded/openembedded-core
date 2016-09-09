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

SRCREV_machine_qemuarm ?= "4db58705727e27bea8ccf805bd5c7d04d2d029ed"
SRCREV_machine_qemuarm64 ?= "93b1a6cee7d4a4437a1c0edf3e54ffe78edb4462"
SRCREV_machine_qemumips ?= "d6237b3b244b894d4b3479ecf37acef041416dfa"
SRCREV_machine_qemuppc ?= "8520e65497ae10e14c38c76920a3457dc64349bf"
SRCREV_machine_qemux86 ?= "93b1a6cee7d4a4437a1c0edf3e54ffe78edb4462"
SRCREV_machine_qemux86-64 ?= "93b1a6cee7d4a4437a1c0edf3e54ffe78edb4462"
SRCREV_machine_qemumips64 ?= "d0524e144cd15a3c0edf768f68400968a4477cbe"
SRCREV_machine ?= "93b1a6cee7d4a4437a1c0edf3e54ffe78edb4462"
SRCREV_meta ?= "b30b6b9ef215433b28e8966c73ebb6b98a7f4d1f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.32"

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
