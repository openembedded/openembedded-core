KBRANCH ?= "v5.4/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH:qemuarm  ?= "v5.4/standard/arm-versatile-926ejs"
KBRANCH:qemuarm64 ?= "v5.4/standard/qemuarm64"
KBRANCH:qemumips ?= "v5.4/standard/mti-malta32"
KBRANCH:qemuppc  ?= "v5.4/standard/qemuppc"
KBRANCH:qemuriscv64  ?= "v5.4/standard/base"
KBRANCH:qemux86  ?= "v5.4/standard/base"
KBRANCH:qemux86-64 ?= "v5.4/standard/base"
KBRANCH:qemumips64 ?= "v5.4/standard/mti-malta64"

SRCREV_machine:qemuarm ?= "3d243cfd29a57dfe6b04a6a6cf1b1088d107f1f6"
SRCREV_machine:qemuarm64 ?= "dcac97a2b4469136189f86fe42703026693384be"
SRCREV_machine:qemumips ?= "ee74231b68518107954078d03d0606910603cf7a"
SRCREV_machine:qemuppc ?= "76c51679aa6b9c25fe2b5c2052e84197ff2e4e2d"
SRCREV_machine:qemuriscv64 ?= "807b4668ff7fe3be031ace442a84d70821ef9571"
SRCREV_machine:qemux86 ?= "807b4668ff7fe3be031ace442a84d70821ef9571"
SRCREV_machine:qemux86-64 ?= "807b4668ff7fe3be031ace442a84d70821ef9571"
SRCREV_machine:qemumips64 ?= "3396071f8ce8ca148231fee4d2130feeead41926"
SRCREV_machine ?= "807b4668ff7fe3be031ace442a84d70821ef9571"
SRCREV_meta ?= "98ba88191b7c489bc0d83b6c87a31b2330fcd886"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE:qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.139"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE:qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64|qemuriscv64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES:append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES:append:qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES:append:qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append:qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES:append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES:append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
