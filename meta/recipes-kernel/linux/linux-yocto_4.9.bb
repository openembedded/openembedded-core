KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/base"
KBRANCH_qemux86-64 ?= "standard/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "bb5856d9b25d13c63a757a408cc8793d6f6302fa"
SRCREV_machine_qemuarm64 ?= "81055b89bd32414ecaf95156ce9a5fa6643e530a"
SRCREV_machine_qemumips ?= "4b150e5ceca8b2c4fb01f74b1f7a57cdeae63762"
SRCREV_machine_qemuppc ?= "81055b89bd32414ecaf95156ce9a5fa6643e530a"
SRCREV_machine_qemux86 ?= "81055b89bd32414ecaf95156ce9a5fa6643e530a"
SRCREV_machine_qemux86-64 ?= "81055b89bd32414ecaf95156ce9a5fa6643e530a"
SRCREV_machine_qemumips64 ?= "b300b94b77ed9fbb1c80a2d2441403ad2b7694a9"
SRCREV_machine ?= "81055b89bd32414ecaf95156ce9a5fa6643e530a"
SRCREV_meta ?= "803b8d600e45afa0375459bf599fe365571a3866"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.21"

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
