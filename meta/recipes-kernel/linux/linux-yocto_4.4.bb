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

SRCREV_machine_qemuarm ?= "8b42f3bbf0818181eb444af119bd295029ea2caf"
SRCREV_machine_qemuarm64 ?= "2854678e4ccbb4bbd1bb4c243ac3e2571ef9834a"
SRCREV_machine_qemumips ?= "0afe3f8c5fb95ed9c9f87f7765be53f6e0b8fad6"
SRCREV_machine_qemuppc ?= "2854678e4ccbb4bbd1bb4c243ac3e2571ef9834a"
SRCREV_machine_qemux86 ?= "2854678e4ccbb4bbd1bb4c243ac3e2571ef9834a"
SRCREV_machine_qemux86-64 ?= "2854678e4ccbb4bbd1bb4c243ac3e2571ef9834a"
SRCREV_machine_qemumips64 ?= "45dd25b50163846c3346b93a21e967c68ab92b26"
SRCREV_machine ?= "2854678e4ccbb4bbd1bb4c243ac3e2571ef9834a"
SRCREV_meta ?= "fbb3579c4011befe15368fea05f600d37b3444ba"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.76"

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
