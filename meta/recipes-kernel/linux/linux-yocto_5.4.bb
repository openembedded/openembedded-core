KBRANCH ?= "v5.4/standard/base"

require recipes-kernel/linux/linux-yocto.inc
include recipes-kernel/linux/cve-exclusion_5.4.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.4/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.4/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.4/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.4/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.4/standard/base"
KBRANCH_qemux86  ?= "v5.4/standard/base"
KBRANCH_qemux86-64 ?= "v5.4/standard/base"
KBRANCH_qemumips64 ?= "v5.4/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "a1fa03030adf951abcd2fc5c44f6133352e452da"
SRCREV_machine_qemuarm64 ?= "31006b756f0b4b686b0fe4fad0f122ad427756de"
SRCREV_machine_qemumips ?= "199be2c0b3869c13ea32737a316e51eca1a3cd6f"
SRCREV_machine_qemuppc ?= "f1575e20066e5f61c363e7fccdcdf8b8ae23a8f3"
SRCREV_machine_qemuriscv64 ?= "698e12267b8f334a5f6a2024e4b9b1f8a95a05ac"
SRCREV_machine_qemux86 ?= "698e12267b8f334a5f6a2024e4b9b1f8a95a05ac"
SRCREV_machine_qemux86-64 ?= "698e12267b8f334a5f6a2024e4b9b1f8a95a05ac"
SRCREV_machine_qemumips64 ?= "a9c1d19cd3d1d0df846cd419cd75cf59995f89eb"
SRCREV_machine ?= "698e12267b8f334a5f6a2024e4b9b1f8a95a05ac"
SRCREV_meta ?= "c841eec84cf56e6b837f12a359c35c5dfb26da5f"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.268"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64|qemuriscv64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "" ,d)}"
