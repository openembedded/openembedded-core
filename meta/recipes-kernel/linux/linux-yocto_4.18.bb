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

SRCREV_machine_qemuarm ?= "285677060e108d00ed786b5347d447807d94a963"
SRCREV_machine_qemuarm64 ?= "2ba9a28434f83c917ad3cfb8b4f7db6066c26a99"
SRCREV_machine_qemumips ?= "cbfe1fd6157ff4a1497dad50164bd493cb680f17"
SRCREV_machine_qemuppc ?= "2ba9a28434f83c917ad3cfb8b4f7db6066c26a99"
SRCREV_machine_qemux86 ?= "2ba9a28434f83c917ad3cfb8b4f7db6066c26a99"
SRCREV_machine_qemux86-64 ?= "2ba9a28434f83c917ad3cfb8b4f7db6066c26a99"
SRCREV_machine_qemumips64 ?= "090c1efbf5d1e9267f73eec102a53ea7b155d7c1"
SRCREV_machine ?= "2ba9a28434f83c917ad3cfb8b4f7db6066c26a99"
SRCREV_meta ?= "ed1978c99214b03740a8989d46f175252435ddc2"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.18;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "4.18.14"

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
