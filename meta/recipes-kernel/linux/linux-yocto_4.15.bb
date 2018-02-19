KBRANCH ?= "v4.15/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.15/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.15/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.15/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.15/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.15/standard/base"
KBRANCH_qemux86-64 ?= "v4.15/standard/base"
KBRANCH_qemumips64 ?= "v4.15/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "312b80a833f6be40034526f3155360f61d78608a"
SRCREV_machine_qemuarm64 ?= "a6a3a6a73d628798fc6eb6832bf829a9ea43f6bd"
SRCREV_machine_qemumips ?= "a52de76fa49f7c93031a807d8668ae2d7c2c4d0a"
SRCREV_machine_qemuppc ?= "a6a3a6a73d628798fc6eb6832bf829a9ea43f6bd"
SRCREV_machine_qemux86 ?= "a6a3a6a73d628798fc6eb6832bf829a9ea43f6bd"
SRCREV_machine_qemux86-64 ?= "a6a3a6a73d628798fc6eb6832bf829a9ea43f6bd"
SRCREV_machine_qemumips64 ?= "3a25d83e268bd34ff12f12011b0b8254e86833a2"
SRCREV_machine ?= "a6a3a6a73d628798fc6eb6832bf829a9ea43f6bd"
SRCREV_meta ?= "030f397472fc8ea8e7f8a069153bc3b6d52f3d45"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.15;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.15.3"

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
