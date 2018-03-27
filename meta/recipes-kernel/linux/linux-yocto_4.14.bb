KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "91bb6027a9a8aff1dd06f4fc4704b9c87c77a7c8"
SRCREV_machine_qemuarm64 ?= "1665bf9e7532c6e9bd59d08c6dffa3819c621195"
SRCREV_machine_qemumips ?= "9e29cfdcd4823d3dec0bb0d803b5e3be2bc73ffa"
SRCREV_machine_qemuppc ?= "e22e4b39c0d97706f0298d8313e0a51ba22af65a"
SRCREV_machine_qemux86 ?= "6ba0d6a225af558654be7619655b0c32ca866bc7"
SRCREV_machine_qemux86-64 ?= "6ba0d6a225af558654be7619655b0c32ca866bc7"
SRCREV_machine_qemumips64 ?= "d8c7e535a8dcc7b30213c628e4c4c98e56daad6b"
SRCREV_machine ?= "6ba0d6a225af558654be7619655b0c32ca866bc7"
SRCREV_meta ?= "95cda8806311f4d922919e2eb6581bcb6d08e7f2"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.24"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
