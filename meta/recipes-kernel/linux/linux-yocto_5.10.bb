KBRANCH ?= "v5.10/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.10/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.10/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.10/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.10/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.10/standard/base"
KBRANCH_qemuriscv32  ?= "v5.10/standard/base"
KBRANCH_qemux86  ?= "v5.10/standard/base"
KBRANCH_qemux86-64 ?= "v5.10/standard/base"
KBRANCH_qemumips64 ?= "v5.10/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "011882741f10bd0c725139baa383eb5a4d833bca"
SRCREV_machine_qemuarm64 ?= "098464b7c0c3d6f2a5b9226aab3245c3fcfb4797"
SRCREV_machine_qemumips ?= "88494c005ab76864e706097056557b5616da65c6"
SRCREV_machine_qemuppc ?= "a2b9c27f431a20ebfcd4146971c9422f4216aa90"
SRCREV_machine_qemuriscv64 ?= "7dda2a9f69de7f80e572d38236896e97be79f39d"
SRCREV_machine_qemuriscv32 ?= "7dda2a9f69de7f80e572d38236896e97be79f39d"
SRCREV_machine_qemux86 ?= "7dda2a9f69de7f80e572d38236896e97be79f39d"
SRCREV_machine_qemux86-64 ?= "7dda2a9f69de7f80e572d38236896e97be79f39d"
SRCREV_machine_qemumips64 ?= "80da62cff32617711767cd6b01f64f1dc14f94d8"
SRCREV_machine ?= "7dda2a9f69de7f80e572d38236896e97be79f39d"
SRCREV_meta ?= "f8afd84b117f336477846b9e22178ebefb26c08d"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.10.70"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
