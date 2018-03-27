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

SRCREV_machine_qemuarm ?= "fa1d1c94af290039f25b2b6fb0f419b4272e7156"
SRCREV_machine_qemuarm64 ?= "97e710ef0545c19d3c10bd81a61bdca9fe543b81"
SRCREV_machine_qemumips ?= "558d4b974687d421c5e8ff63a7d2660520f9dab0"
SRCREV_machine_qemuppc ?= "97e710ef0545c19d3c10bd81a61bdca9fe543b81"
SRCREV_machine_qemux86 ?= "97e710ef0545c19d3c10bd81a61bdca9fe543b81"
SRCREV_machine_qemux86-64 ?= "97e710ef0545c19d3c10bd81a61bdca9fe543b81"
SRCREV_machine_qemumips64 ?= "09bddd16543c2f4fa1bb5a535994975dd1457fe2"
SRCREV_machine ?= "97e710ef0545c19d3c10bd81a61bdca9fe543b81"
SRCREV_meta ?= "f8f75cc4d9cb3195a2221b375cfc4f91ee211399"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

DEPENDS += "openssl-native util-linux-native"

LINUX_VERSION ?= "4.12.21"

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
