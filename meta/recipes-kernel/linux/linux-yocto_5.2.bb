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

SRCREV_machine_qemuarm ?= "b1a5d8b801acdd81240824e167126adb3cdcd9f1"
SRCREV_machine_qemuarm64 ?= "c225913886b6aff4372d209f41f59cee4eeae724"
SRCREV_machine_qemumips ?= "26055f7dcb659edd43b21519019d4773188570c7"
SRCREV_machine_qemuppc ?= "c225913886b6aff4372d209f41f59cee4eeae724"
SRCREV_machine_qemuriscv64 ?= "c225913886b6aff4372d209f41f59cee4eeae724"
SRCREV_machine_qemux86 ?= "c225913886b6aff4372d209f41f59cee4eeae724"
SRCREV_machine_qemux86-64 ?= "c225913886b6aff4372d209f41f59cee4eeae724"
SRCREV_machine_qemumips64 ?= "1266ed0842cc08adba6086ec4af268bc87a576a4"
SRCREV_machine ?= "c225913886b6aff4372d209f41f59cee4eeae724"
SRCREV_meta ?= "1763be055f7ff1a3215c2d068f1df1b08ad5ac70"

# remap qemuarm to qemuarma15 for the 5.2 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.2;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.2.14"

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
