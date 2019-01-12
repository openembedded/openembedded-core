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

SRCREV_machine_qemuarm ?= "8b378ac8aca51207085c8df616a6fe56af19f0b4"
SRCREV_machine_qemuarm64 ?= "1114697c04b9699112e78dd8fc6ec849609ca1cb"
SRCREV_machine_qemumips ?= "58f59c2213a657515a158379db153ad618986235"
SRCREV_machine_qemuppc ?= "1114697c04b9699112e78dd8fc6ec849609ca1cb"
SRCREV_machine_qemux86 ?= "1114697c04b9699112e78dd8fc6ec849609ca1cb"
SRCREV_machine_qemux86-64 ?= "1114697c04b9699112e78dd8fc6ec849609ca1cb"
SRCREV_machine_qemumips64 ?= "6f56a58c214379efe8949da6921b1c00c2af353a"
SRCREV_machine ?= "1114697c04b9699112e78dd8fc6ec849609ca1cb"
SRCREV_meta ?= "096c8bd3bd11801f95683bbe4bcc885a2318f51d"

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
