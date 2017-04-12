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

SRCREV_machine_qemuarm ?= "718b0a546417a51a186179d5277713f301e1d7c9"
SRCREV_machine_qemuarm64 ?= "fe0fb8da3d633d802776081e7ff2df73ff2983f2"
SRCREV_machine_qemumips ?= "a803c18e328080eb9acc71510df3b7567bd87578"
SRCREV_machine_qemuppc ?= "fe0fb8da3d633d802776081e7ff2df73ff2983f2"
SRCREV_machine_qemux86 ?= "fe0fb8da3d633d802776081e7ff2df73ff2983f2"
SRCREV_machine_qemux86-64 ?= "fe0fb8da3d633d802776081e7ff2df73ff2983f2"
SRCREV_machine_qemumips64 ?= "97b912becdd8322f4e7f45dfe79e37fc5ea35f88"
SRCREV_machine ?= "fe0fb8da3d633d802776081e7ff2df73ff2983f2"
SRCREV_meta ?= "ad2e885015c5bf8adeef5cb789b535fc3c0dd396"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.10.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.10;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.10.9"

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
