KBRANCH ?= "v4.19/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.19/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.19/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.19/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.19/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.19/standard/base"
KBRANCH_qemux86-64 ?= "v4.19/standard/base"
KBRANCH_qemumips64 ?= "v4.19/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "a14b08c3446dfab88ff97d84a9ba70db11287290"
SRCREV_machine_qemuarm64 ?= "ba6690e7b65aaa1d1030e51498aaf2c5eb7e88b1"
SRCREV_machine_qemumips ?= "69975293fac754567a3ebdd0aa914d461e218b70"
SRCREV_machine_qemuppc ?= "ba6690e7b65aaa1d1030e51498aaf2c5eb7e88b1"
SRCREV_machine_qemux86 ?= "ba6690e7b65aaa1d1030e51498aaf2c5eb7e88b1"
SRCREV_machine_qemux86-64 ?= "ba6690e7b65aaa1d1030e51498aaf2c5eb7e88b1"
SRCREV_machine_qemumips64 ?= "8c14832341502679afbfb370d48b977ecf363c82"
SRCREV_machine ?= "ba6690e7b65aaa1d1030e51498aaf2c5eb7e88b1"
SRCREV_meta ?= "26bfb042ebb6e4816c5a8ce85d97a55e763430c7"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.19;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "4.19.5"

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
