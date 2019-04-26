KBRANCH ?= "v5.0/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.0/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.0/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.0/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.0/standard/qemuppc"
KBRANCH_qemux86  ?= "v5.0/standard/base"
KBRANCH_qemux86-64 ?= "v5.0/standard/base"
KBRANCH_qemumips64 ?= "v5.0/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "ed9d11e2c8ebbfe056420cb89c2e5d02836496ce"
SRCREV_machine_qemuarm64 ?= "9c40ed0d86ad87f48659aad4fdead2455e8b5db8"
SRCREV_machine_qemumips ?= "40a729c3b0683d0875f8d6ad7353e6e429c7afc2"
SRCREV_machine_qemuppc ?= "9c40ed0d86ad87f48659aad4fdead2455e8b5db8"
SRCREV_machine_qemux86 ?= "9c40ed0d86ad87f48659aad4fdead2455e8b5db8"
SRCREV_machine_qemux86-64 ?= "9c40ed0d86ad87f48659aad4fdead2455e8b5db8"
SRCREV_machine_qemumips64 ?= "437d99225c689f3f192bb834e4d649ea0467ac87"
SRCREV_machine ?= "9c40ed0d86ad87f48659aad4fdead2455e8b5db8"
SRCREV_meta ?= "ffd8cf5baf8e741b8987b72c942ce3b9cc7c7f30"

# remap qemuarm to qemuarma15 for the 5.0 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.0;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.0.7"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
