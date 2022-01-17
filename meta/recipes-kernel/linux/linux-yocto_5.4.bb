KBRANCH ?= "v5.4/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.4/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.4/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.4/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.4/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.4/standard/base"
KBRANCH_qemux86  ?= "v5.4/standard/base"
KBRANCH_qemux86-64 ?= "v5.4/standard/base"
KBRANCH_qemumips64 ?= "v5.4/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "69471f4c5c895fe6ccef696800c7ef2bda3ad2fd"
SRCREV_machine_qemuarm64 ?= "e0b9164ac4b0d53e7bd42bf1d2322eb5ce462d68"
SRCREV_machine_qemumips ?= "7bf5ddb8e9ae1284cb4e02bed7f9429bec0b39c4"
SRCREV_machine_qemuppc ?= "b0054e5e7451561a7c6a6d6a401395fbd0395801"
SRCREV_machine_qemuriscv64 ?= "01fe83c50f9aeb4da7c7c7d63a6c7afea83216ef"
SRCREV_machine_qemux86 ?= "01fe83c50f9aeb4da7c7c7d63a6c7afea83216ef"
SRCREV_machine_qemux86-64 ?= "01fe83c50f9aeb4da7c7c7d63a6c7afea83216ef"
SRCREV_machine_qemumips64 ?= "e8483ea124a1715790eba584ee2ad8aac1e15edf"
SRCREV_machine ?= "01fe83c50f9aeb4da7c7c7d63a6c7afea83216ef"
SRCREV_meta ?= "dcbd44e70b6bc80a04cc92b625b1a3eaa2f78fc0"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.169"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64|qemuriscv64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
