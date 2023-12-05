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

SRCREV_machine_qemuarm ?= "02d0d86cd3148f12cccb7af027fb41d41b03c726"
SRCREV_machine_qemuarm64 ?= "5880452e43a9cf56108492a627b85196ea10190b"
SRCREV_machine_qemumips ?= "9754f47861b4a29b4c4577ce14b7fb44c81c521e"
SRCREV_machine_qemuppc ?= "3e5d9bf09fb23b0acefe73c644de0028dd51fe42"
SRCREV_machine_qemuriscv64 ?= "d55e21e23f5d099d7c8ad58d5c2b6302c02ef9f0"
SRCREV_machine_qemux86 ?= "d55e21e23f5d099d7c8ad58d5c2b6302c02ef9f0"
SRCREV_machine_qemux86-64 ?= "d55e21e23f5d099d7c8ad58d5c2b6302c02ef9f0"
SRCREV_machine_qemumips64 ?= "98065f598767e0f92ed86f880795603df7a3a9d2"
SRCREV_machine ?= "d55e21e23f5d099d7c8ad58d5c2b6302c02ef9f0"
SRCREV_meta ?= "0bc097b782b9f41b41b31074cdbd86b8e7393209"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.258"

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
