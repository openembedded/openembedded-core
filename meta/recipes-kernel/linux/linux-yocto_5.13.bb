KBRANCH ?= "v5.13/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v5.13/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v5.13/standard/qemuarm64"
KBRANCH_qemumips ?= "v5.13/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v5.13/standard/qemuppc"
KBRANCH_qemuriscv64  ?= "v5.13/standard/base"
KBRANCH_qemuriscv32  ?= "v5.13/standard/base"
KBRANCH_qemux86  ?= "v5.13/standard/base"
KBRANCH_qemux86-64 ?= "v5.13/standard/base"
KBRANCH_qemumips64 ?= "v5.13/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "9c2fd4b59ebf57f1900f82d49d6bf4fe074cf818"
SRCREV_machine_qemuarm64 ?= "b1cead8d98582ca687f93e06438543b97144e5bf"
SRCREV_machine_qemumips ?= "1ea414ef9e3a9744ec09f7ecebcc3b9aeffe39d0"
SRCREV_machine_qemuppc ?= "b1cead8d98582ca687f93e06438543b97144e5bf"
SRCREV_machine_qemuriscv64 ?= "b1cead8d98582ca687f93e06438543b97144e5bf"
SRCREV_machine_qemuriscv32 ?= "b1cead8d98582ca687f93e06438543b97144e5bf"
SRCREV_machine_qemux86 ?= "b1cead8d98582ca687f93e06438543b97144e5bf"
SRCREV_machine_qemux86-64 ?= "b1cead8d98582ca687f93e06438543b97144e5bf"
SRCREV_machine_qemumips64 ?= "b74fe3dcca0653609fcb75aad883b1db07619081"
SRCREV_machine ?= "b1cead8d98582ca687f93e06438543b97144e5bf"
SRCREV_meta ?= "ceb5fa598d08902fe2934c041875aa92d9a6fa19"

# remap qemuarm to qemuarma15 for the 5.8 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.13;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
LINUX_VERSION ?= "5.13"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "1"

KERNEL_DEVICETREE_qemuarmv5 = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarmv5|qemuarm64|qemux86|qemuppc|qemuppc64|qemumips|qemumips64|qemux86-64|qemuriscv64|qemuriscv32"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc features/drm-bochs/drm-bochs.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/scsi/scsi-debug.scc", "", d)}"
KERNEL_FEATURES_append = " ${@bb.utils.contains("DISTRO_FEATURES", "ptest", " features/gpio/mockup.scc", "", d)}"
