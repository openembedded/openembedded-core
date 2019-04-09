KBRANCH ?= "v4.18/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.18/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.18/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.18/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.18/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.18/standard/base"
KBRANCH_qemux86-64 ?= "v4.18/standard/base"
KBRANCH_qemumips64 ?= "v4.18/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "813d81df5defc4e552b7c3085673437eaba4eea7"
SRCREV_machine_qemuarm64 ?= "1a564c76f41cff5c9e9011c4dbb5ef8453836b5d"
SRCREV_machine_qemumips ?= "12620f16df220007dfda3b70cc5044ec2322142c"
SRCREV_machine_qemuppc ?= "1a564c76f41cff5c9e9011c4dbb5ef8453836b5d"
SRCREV_machine_qemux86 ?= "1a564c76f41cff5c9e9011c4dbb5ef8453836b5d"
SRCREV_machine_qemux86-64 ?= "1a564c76f41cff5c9e9011c4dbb5ef8453836b5d"
SRCREV_machine_qemumips64 ?= "104cdfbbf95981e31a32dfe3bdeaff3afb517ad4"
SRCREV_machine ?= "1a564c76f41cff5c9e9011c4dbb5ef8453836b5d"
SRCREV_meta ?= "865683fc87deee7030cd168f295e8afd70894d6c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.18;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "4.18.33"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
