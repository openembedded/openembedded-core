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

SRCREV_machine_qemuarm ?= "683a13656dad8cac542d5b2eac5f40f00d16010d"
SRCREV_machine_qemuarm64 ?= "b799502f8b1f8804ec9e436c95129be76c548d37"
SRCREV_machine_qemumips ?= "abdd096fd6503fdbdf7d5c81948a07d5a8fe8ea2"
SRCREV_machine_qemuppc ?= "f6e6a1a81c0c3f11c5e42bde6d107c9f0ea45f16"
SRCREV_machine_qemuriscv64 ?= "8151429f787ee936c9a960e8db6f5909e81bae43"
SRCREV_machine_qemuriscv32 ?= "8151429f787ee936c9a960e8db6f5909e81bae43"
SRCREV_machine_qemux86 ?= "8151429f787ee936c9a960e8db6f5909e81bae43"
SRCREV_machine_qemux86-64 ?= "8151429f787ee936c9a960e8db6f5909e81bae43"
SRCREV_machine_qemumips64 ?= "01bad0d6e0916ce3eae77cbdfc64a141a4ae0ecd"
SRCREV_machine ?= "8151429f787ee936c9a960e8db6f5909e81bae43"
SRCREV_meta ?= "201aedddc2f13640a14cbe3ef229c8f9c78bde07"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.10;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.10.49"

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
