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

SRCREV_machine_qemuarm ?= "395c666f57d262a7a7c3553fd0eb40b8339cd9fe"
SRCREV_machine_qemuarm64 ?= "08b83b0eb0ff8fe7a8dae5aa4ef7f8eec7542806"
SRCREV_machine_qemumips ?= "fe7fedf978258fca785ceae508f87f6563c37a27"
SRCREV_machine_qemuppc ?= "08b83b0eb0ff8fe7a8dae5aa4ef7f8eec7542806"
SRCREV_machine_qemuriscv64 ?= "08b83b0eb0ff8fe7a8dae5aa4ef7f8eec7542806"
SRCREV_machine_qemux86 ?= "08b83b0eb0ff8fe7a8dae5aa4ef7f8eec7542806"
SRCREV_machine_qemux86-64 ?= "08b83b0eb0ff8fe7a8dae5aa4ef7f8eec7542806"
SRCREV_machine_qemumips64 ?= "536bda19159206ac7f60165f71bbf892cb8ad3fd"
SRCREV_machine ?= "08b83b0eb0ff8fe7a8dae5aa4ef7f8eec7542806"
SRCREV_meta ?= "8df18409861d25d9ca84b80c9ca8181a48f6ee45"

# remap qemuarm to qemuarma15 for the 5.4 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.4;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.4.73"

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
