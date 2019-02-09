KBRANCH ?= "v4.19/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.19/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.19/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.19/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.19/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.19/standard/base"
KBRANCH_qemux86-64 ?= "v4.19/standard/base"
KBRANCH_qemumips64 ?= "v4.19/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "0d83500d7377b68aeb42387350bb41dd7dc04315"
SRCREV_machine_qemuarm64 ?= "11e0e616ed095bb8012e1b4a231254c9656a0193"
SRCREV_machine_qemumips ?= "0169796412b83d8835f3f9b81d19efe008199fdd"
SRCREV_machine_qemuppc ?= "11e0e616ed095bb8012e1b4a231254c9656a0193"
SRCREV_machine_qemux86 ?= "11e0e616ed095bb8012e1b4a231254c9656a0193"
SRCREV_machine_qemux86-64 ?= "11e0e616ed095bb8012e1b4a231254c9656a0193"
SRCREV_machine_qemumips64 ?= "fc7a2bf24e0e39b6a260f8650a3c8d497b8cdcfd"
SRCREV_machine ?= "11e0e616ed095bb8012e1b4a231254c9656a0193"
SRCREV_meta ?= "8d7d003531151ba0a0e0cdb3cefa0bf9b0e84b55"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.19;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "4.19.19"

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
