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

SRCREV_machine_qemuarm ?= "7e4e6b59cac3b71d43a587728acc7bfe78ef16af"
SRCREV_machine_qemuarm64 ?= "234f5db38e546134931e1e6ffb3677e5516704fc"
SRCREV_machine_qemumips ?= "60a93a5cfd6f6a5c7374e09d234ceb234367270d"
SRCREV_machine_qemuppc ?= "63a0e4217eaa70fa4fc7dacf2a9356e88c79f80e"
SRCREV_machine_qemuriscv64 ?= "abf55d087ff6e9e2cd03499a0eff130b1afc8851"
SRCREV_machine_qemux86 ?= "abf55d087ff6e9e2cd03499a0eff130b1afc8851"
SRCREV_machine_qemux86-64 ?= "abf55d087ff6e9e2cd03499a0eff130b1afc8851"
SRCREV_machine_qemumips64 ?= "9bc9e7c0b392466df817283ea0f05c7cf330f8a5"
SRCREV_machine ?= "abf55d087ff6e9e2cd03499a0eff130b1afc8851"
SRCREV_meta ?= "a1399e88272ab04a7748051822d5b16b9b1d3f9c"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.265"

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
