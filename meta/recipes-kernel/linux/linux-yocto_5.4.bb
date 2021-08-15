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

SRCREV_machine_qemuarm ?= "88d3d4ec2c23ed165ee11dd9fc48ac77fee480ba"
SRCREV_machine_qemuarm64 ?= "7cad0ac3beb6fc0a1f84dc079d0e6a68fedb3df4"
SRCREV_machine_qemumips ?= "d8b1cb081cbfa6a7157561d61ce383bc661208d8"
SRCREV_machine_qemuppc ?= "528a4de381c0a22e9eeebe899e159ed8d4fccff3"
SRCREV_machine_qemuriscv64 ?= "90d297d951740cd13553072fb00afbf39045d38a"
SRCREV_machine_qemux86 ?= "90d297d951740cd13553072fb00afbf39045d38a"
SRCREV_machine_qemux86-64 ?= "90d297d951740cd13553072fb00afbf39045d38a"
SRCREV_machine_qemumips64 ?= "29b36c7b1c51b5322cba165dd11b435ad9439adf"
SRCREV_machine ?= "90d297d951740cd13553072fb00afbf39045d38a"
SRCREV_meta ?= "cfd547115bc4d65c29f0f4313bd950976f41ebd8"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.137"

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
