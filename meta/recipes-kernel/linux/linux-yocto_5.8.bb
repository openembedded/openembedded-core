KBRANCH ?= "v5.8/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.8/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.8/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.8/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.8/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.8/standard/base"
KBRANCH_qemux86  ?= "v5.8/standard/base"
KBRANCH_qemux86-64 ?= "v5.8/standard/base"
KBRANCH_qemumips64 ?= "v5.8/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "3bb879f7650c06b97ee4d4b496d33bb5d62a4679"
SRCREV_machine_qemuarm64 ?= "d0ff030adc1f65129e29c7110c9be2adaa0533f2"
SRCREV_machine_qemumips ?= "6dd61d0707b17293a472f5717acb68d5a86e1e4d"
SRCREV_machine_qemuppc ?= "d0ff030adc1f65129e29c7110c9be2adaa0533f2"
SRCREV_machine_qemuriscv64 ?= "d0ff030adc1f65129e29c7110c9be2adaa0533f2"
SRCREV_machine_qemux86 ?= "d0ff030adc1f65129e29c7110c9be2adaa0533f2"
SRCREV_machine_qemux86-64 ?= "d0ff030adc1f65129e29c7110c9be2adaa0533f2"
SRCREV_machine_qemumips64 ?= "a92b9df163d6e6b05bf1e7137d6868760537f326"
SRCREV_machine ?= "d0ff030adc1f65129e29c7110c9be2adaa0533f2"
SRCREV_meta ?= "cc4fd81dde0725d813f988fe3dd4787d2084f997"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.8;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.8.17"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

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
