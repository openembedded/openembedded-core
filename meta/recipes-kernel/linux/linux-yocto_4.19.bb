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

SRCREV_machine_qemuarm ?= "ec8e55a97ad8a88107c5af069ece1b7f203f4ac4"
SRCREV_machine_qemuarm64 ?= "e727c70583a78cf1dc53e8e219722e2f9d1bc858"
SRCREV_machine_qemumips ?= "3b5fba0207a870c1de76c5dd80d8d3c0e42aa1cf"
SRCREV_machine_qemuppc ?= "e727c70583a78cf1dc53e8e219722e2f9d1bc858"
SRCREV_machine_qemux86 ?= "e727c70583a78cf1dc53e8e219722e2f9d1bc858"
SRCREV_machine_qemux86-64 ?= "e727c70583a78cf1dc53e8e219722e2f9d1bc858"
SRCREV_machine_qemumips64 ?= "d21ff98864241c971c7a42a3c4f1eb940b9b8855"
SRCREV_machine ?= "e727c70583a78cf1dc53e8e219722e2f9d1bc858"
SRCREV_meta ?= "085d8f700a427a0c07e01339915ac04f501ecd29"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.19;destsuffix=${KMETA}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
LINUX_VERSION ?= "4.19.8"

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
