KBRANCH ?= "v4.15/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.15/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.15/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.15/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.15/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.15/standard/base"
KBRANCH_qemux86-64 ?= "v4.15/standard/base"
KBRANCH_qemumips64 ?= "v4.15/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "12c2b0077b13cf6a2f1096a0d51f3a6ed5cbbd3a"
SRCREV_machine_qemuarm64 ?= "3373c0cf71f2812eeb9694839456df6f67fd32ac"
SRCREV_machine_qemumips ?= "c03f2971b07a79c5402f0d7126cc2cd856028e38"
SRCREV_machine_qemuppc ?= "3373c0cf71f2812eeb9694839456df6f67fd32ac"
SRCREV_machine_qemux86 ?= "3373c0cf71f2812eeb9694839456df6f67fd32ac"
SRCREV_machine_qemux86-64 ?= "3373c0cf71f2812eeb9694839456df6f67fd32ac"
SRCREV_machine_qemumips64 ?= "b98c113571c72a0d34832dd6fe5585529b32988b"
SRCREV_machine ?= "3373c0cf71f2812eeb9694839456df6f67fd32ac"
SRCREV_meta ?= "ff65711e13fa39d7fe2947a1ba196dad82eed327"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.15;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.15.18"

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
