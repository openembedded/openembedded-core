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

SRCREV_machine_qemuarm ?= "fcd7089abefcf3025b6a49baa0604fb7215886ce"
SRCREV_machine_qemuarm64 ?= "30ecfd434ced3ee254e669b8af061cd1ab8155e7"
SRCREV_machine_qemumips ?= "30bbc3b38f5caa851d01b37ab1cdd2dae75e9592"
SRCREV_machine_qemuppc ?= "30ecfd434ced3ee254e669b8af061cd1ab8155e7"
SRCREV_machine_qemuriscv64 ?= "30ecfd434ced3ee254e669b8af061cd1ab8155e7"
SRCREV_machine_qemux86 ?= "30ecfd434ced3ee254e669b8af061cd1ab8155e7"
SRCREV_machine_qemux86-64 ?= "30ecfd434ced3ee254e669b8af061cd1ab8155e7"
SRCREV_machine_qemumips64 ?= "4dab38a25c5b32491b23cd95eca75a68f02b3339"
SRCREV_machine ?= "30ecfd434ced3ee254e669b8af061cd1ab8155e7"
SRCREV_meta ?= "57c5933eb4c46ddaeee331717360e68380724126"

# remap qemuarm to qemuarma15 for the 5.2 kernel
# KMACHINE_qemuarm ?= "qemuarma15"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-5.2;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "5.2.21"

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
