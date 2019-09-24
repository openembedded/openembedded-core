KBRANCH ?= "v5.2/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.2/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.2/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.2/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.2/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.2/standard/base"
KBRANCH_qemux86  ?= "v5.2/standard/base"
KBRANCH_qemux86-64 ?= "v5.2/standard/base"
KBRANCH_qemumips64 ?= "v5.2/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "279084b42cecd49010a59e2850887f39772226d8"
SRCREV_machine_qemuarm64 ?= "23cc3d3314b5d3482c595baaa036edd2b6d1b69b"
SRCREV_machine_qemumips ?= "e4e184d31c8e43987077a47d84d8e40963e4bf70"
SRCREV_machine_qemuppc ?= "23cc3d3314b5d3482c595baaa036edd2b6d1b69b"
SRCREV_machine_qemuriscv64 ?= "23cc3d3314b5d3482c595baaa036edd2b6d1b69b"
SRCREV_machine_qemux86 ?= "23cc3d3314b5d3482c595baaa036edd2b6d1b69b"
SRCREV_machine_qemux86-64 ?= "23cc3d3314b5d3482c595baaa036edd2b6d1b69b"
SRCREV_machine_qemumips64 ?= "cf8ce04ac2a888234fbd168c7208504f632407fa"
SRCREV_machine ?= "23cc3d3314b5d3482c595baaa036edd2b6d1b69b"
SRCREV_meta ?= "3f2c84ec315b2907de990a5097efe23c605d9e51"

# remap qemuarm to qemuarma15 for the 5.2 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.2;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.2.16"

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
