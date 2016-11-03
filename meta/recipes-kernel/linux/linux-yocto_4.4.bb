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

SRCREV_machine_qemuarm ?= "d752c82364bf890681c161d80717d174419e7512"
SRCREV_machine_qemuarm64 ?= "3c15255fd62c2202d76b5c110265f16d33010b9d"
SRCREV_machine_qemumips ?= "ebf27c56cf862b2d5fd08e229e027b5e4dff3609"
SRCREV_machine_qemuppc ?= "3c15255fd62c2202d76b5c110265f16d33010b9d"
SRCREV_machine_qemux86 ?= "3c15255fd62c2202d76b5c110265f16d33010b9d"
SRCREV_machine_qemux86-64 ?= "3c15255fd62c2202d76b5c110265f16d33010b9d"
SRCREV_machine_qemumips64 ?= "de5b483095712c0c347689ef98e2a9b95bed4c7a"
SRCREV_machine ?= "3c15255fd62c2202d76b5c110265f16d33010b9d"
SRCREV_meta ?= "d2d1decbd11e8f78b1aee36605d3653015d710e5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.30"

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
