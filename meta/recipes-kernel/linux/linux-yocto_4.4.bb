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

SRCREV_machine_qemuarm ?= "50253831d9b2d3dfb8e910debda66405cb11900a"
SRCREV_machine_qemuarm64 ?= "0327f8213797c4885f86aec26cf55aeef5834180"
SRCREV_machine_qemumips ?= "d49ac34e4781b4bcd4c1728338668bb1fd321f7c"
SRCREV_machine_qemuppc ?= "0327f8213797c4885f86aec26cf55aeef5834180"
SRCREV_machine_qemux86 ?= "0327f8213797c4885f86aec26cf55aeef5834180"
SRCREV_machine_qemux86-64 ?= "0327f8213797c4885f86aec26cf55aeef5834180"
SRCREV_machine_qemumips64 ?= "310df4a803d8938415e761668426391985193398"
SRCREV_machine ?= "0327f8213797c4885f86aec26cf55aeef5834180"
SRCREV_meta ?= "8e9afd032ff3be672506a0e5ed51cde9dc45031f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.85"

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
