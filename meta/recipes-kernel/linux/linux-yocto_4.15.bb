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

SRCREV_machine_qemuarm ?= "5a2a8f7f757b964da55bd3d2a95130051af2c173"
SRCREV_machine_qemuarm64 ?= "9f9649fffac97603862fdc90530e0b3ff038eed1"
SRCREV_machine_qemumips ?= "ee76cb3c65f9138c9e580c13feff6d0c6ebf5f9a"
SRCREV_machine_qemuppc ?= "9f9649fffac97603862fdc90530e0b3ff038eed1"
SRCREV_machine_qemux86 ?= "9f9649fffac97603862fdc90530e0b3ff038eed1"
SRCREV_machine_qemux86-64 ?= "9f9649fffac97603862fdc90530e0b3ff038eed1"
SRCREV_machine_qemumips64 ?= "3b587c8f1db7c5b425b35a98276c6d9b8dd61a2b"
SRCREV_machine ?= "9f9649fffac97603862fdc90530e0b3ff038eed1"
SRCREV_meta ?= "ac4dd298328c282f5e70b3ddb469edcad57590cf"

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
