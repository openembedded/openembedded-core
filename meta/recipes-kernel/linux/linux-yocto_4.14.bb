KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "e4e2990af921c2d1544d18efa5f7183f95289cd0"
SRCREV_machine_qemuarm64 ?= "51c9e69ebef5d2d15dfbcdf098269d86e0e38317"
SRCREV_machine_qemumips ?= "e70c76a3fe9cc785619d9e4c8e28cb4d4d76ecaf"
SRCREV_machine_qemuppc ?= "6b6eab44d3a04294c233e0b47d6b7c6cbb6e9ffb"
SRCREV_machine_qemux86 ?= "57278e88a6b0f7c6230f7429cab7e74229f2b7ce"
SRCREV_machine_qemux86-64 ?= "57278e88a6b0f7c6230f7429cab7e74229f2b7ce"
SRCREV_machine_qemumips64 ?= "4e099e87d223bfc1526543a5e4c5383cb2edda70"
SRCREV_machine ?= "57278e88a6b0f7c6230f7429cab7e74229f2b7ce"
SRCREV_meta ?= "a889c43359ca8bee705601817c50edf3c209bc09"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.154"

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
