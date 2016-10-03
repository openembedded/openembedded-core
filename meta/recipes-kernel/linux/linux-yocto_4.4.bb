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

SRCREV_machine_qemuarm ?= "22a817dc851fac7ec9369c29b3a2803ec92781fb"
SRCREV_machine_qemuarm64 ?= "0ef03dcfb96805e6bcf82989f1c8fed1b488d00a"
SRCREV_machine_qemumips ?= "c1794372ab031297d1c4482d982e143386c247ea"
SRCREV_machine_qemuppc ?= "0ef03dcfb96805e6bcf82989f1c8fed1b488d00a"
SRCREV_machine_qemux86 ?= "0ef03dcfb96805e6bcf82989f1c8fed1b488d00a"
SRCREV_machine_qemux86-64 ?= "0ef03dcfb96805e6bcf82989f1c8fed1b488d00a"
SRCREV_machine_qemumips64 ?= "ab08db0be1aec0daa5ac134fe1abcd6eba47b275"
SRCREV_machine ?= "0ef03dcfb96805e6bcf82989f1c8fed1b488d00a"
SRCREV_meta ?= "170ed03a9d908e2abc8025cf9a62789ab15b3ece"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.22"

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
