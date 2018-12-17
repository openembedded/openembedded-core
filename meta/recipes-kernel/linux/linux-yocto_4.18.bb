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

SRCREV_machine_qemuarm ?= "fc10a65e12d9c6d1e0c63e19688727bc72922cdd"
SRCREV_machine_qemuarm64 ?= "e78a46f375febc5f9305b6f94ae12fbf00b46330"
SRCREV_machine_qemumips ?= "d3ecc257838be6d1a15e1eb114ee960283bacf33"
SRCREV_machine_qemuppc ?= "e78a46f375febc5f9305b6f94ae12fbf00b46330"
SRCREV_machine_qemux86 ?= "e78a46f375febc5f9305b6f94ae12fbf00b46330"
SRCREV_machine_qemux86-64 ?= "e78a46f375febc5f9305b6f94ae12fbf00b46330"
SRCREV_machine_qemumips64 ?= "48c3bf17f2c4699c9ee442c62ecdaad85fdb4232"
SRCREV_machine ?= "e78a46f375febc5f9305b6f94ae12fbf00b46330"
SRCREV_meta ?= "5a99020e693eebe662ae0c4c08b65afedbd5cde3"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.18;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "4.18.17"

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
