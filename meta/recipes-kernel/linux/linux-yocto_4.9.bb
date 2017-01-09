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

SRCREV_machine_qemuarm ?= "4f616cec971fc96f282730a7db685638ac530b36"
SRCREV_machine_qemuarm64 ?= "441291bf1bfbbfff1dc1c326b950d92e3cd5c900"
SRCREV_machine_qemumips ?= "51e43e95d3ba96e95b19027d288d24d5e7eaff66"
SRCREV_machine_qemuppc ?= "441291bf1bfbbfff1dc1c326b950d92e3cd5c900"
SRCREV_machine_qemux86 ?= "441291bf1bfbbfff1dc1c326b950d92e3cd5c900"
SRCREV_machine_qemux86-64 ?= "441291bf1bfbbfff1dc1c326b950d92e3cd5c900"
SRCREV_machine_qemumips64 ?= "608c940901025464daaafe9749727ce26a194429"
SRCREV_machine ?= "441291bf1bfbbfff1dc1c326b950d92e3cd5c900"
SRCREV_meta ?= "155d2810faa1977c6a87f671cdb0da48c36af5fd"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.2"

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
