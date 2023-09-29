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

SRCREV_machine_qemuarm ?= "af8795f548930f376f648b3c38c96ea9adeca302"
SRCREV_machine_qemuarm64 ?= "08b2d42ab0000a6f12d816c0828632c162f5173a"
SRCREV_machine_qemumips ?= "37c8da56986328d9030015e1a80beaa90babab30"
SRCREV_machine_qemuppc ?= "3cd238f6056560888f7f717c569ca4a1fe16ccc9"
SRCREV_machine_qemuriscv64 ?= "aee8802f6fec35ea9b393707cc2adb4d433d93c8"
SRCREV_machine_qemux86 ?= "aee8802f6fec35ea9b393707cc2adb4d433d93c8"
SRCREV_machine_qemux86-64 ?= "aee8802f6fec35ea9b393707cc2adb4d433d93c8"
SRCREV_machine_qemumips64 ?= "44fbd145164885c2ba73a8ddcb09fd6f3ab0d59c"
SRCREV_machine ?= "aee8802f6fec35ea9b393707cc2adb4d433d93c8"
SRCREV_meta ?= "3f4db4c1957e98a3da50908339aaee426e58fd13"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.257"

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
