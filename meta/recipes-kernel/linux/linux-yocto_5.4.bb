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

SRCREV_machine_qemuarm ?= "b7e0891bf4b281c4e29b86f708e10a3339670acc"
SRCREV_machine_qemuarm64 ?= "ff75f0c7beb167391f0285dd2993394cd143a8a7"
SRCREV_machine_qemumips ?= "650e43a19e625d1db9d8245cda27db7b86990398"
SRCREV_machine_qemuppc ?= "0fb6546a09f90befecb11cd0f10274276e8a3021"
SRCREV_machine_qemuriscv64 ?= "fe901e2f4b156e9cf7ddb03f479f7339d28e398b"
SRCREV_machine_qemux86 ?= "fe901e2f4b156e9cf7ddb03f479f7339d28e398b"
SRCREV_machine_qemux86-64 ?= "fe901e2f4b156e9cf7ddb03f479f7339d28e398b"
SRCREV_machine_qemumips64 ?= "f59947f338319b1741db5dfac34f08399561ab25"
SRCREV_machine ?= "fe901e2f4b156e9cf7ddb03f479f7339d28e398b"
SRCREV_meta ?= "ecd382f3477fae022ad1881e4c39e810cdc3c760"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.273"

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
