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

SRCREV_machine_qemuarm ?= "493254190708d94e0bd22f6eb6e6c17cbdbf9764"
SRCREV_machine_qemuarm64 ?= "eeba95930402de2c0290b423c69e86b84955bf09"
SRCREV_machine_qemumips ?= "42b5e2db78595024d2fddfcadc8cd8af1266b52b"
SRCREV_machine_qemuppc ?= "b87ce52358105e318fbdb7087185de9622b420b3"
SRCREV_machine_qemux86 ?= "0e21580445573276ec320da0c36d56f8921f682b"
SRCREV_machine_qemux86-64 ?= "0e21580445573276ec320da0c36d56f8921f682b"
SRCREV_machine_qemumips64 ?= "c8885bcf579812bd6cfbb58b96d5d3423ccc45fc"
SRCREV_machine ?= "0e21580445573276ec320da0c36d56f8921f682b"
SRCREV_meta ?= "d1e170692e978b08f4736375cafccd58e331feb1"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.40"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

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
