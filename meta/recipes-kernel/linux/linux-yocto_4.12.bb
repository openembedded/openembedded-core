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

SRCREV_machine_qemuarm ?= "dafca619b2bd44966042bba76aa00edb0ca002df"
SRCREV_machine_qemuarm64 ?= "257f843ea367744620f1d92910afd2f454e31483"
SRCREV_machine_qemumips ?= "99c30bfd13f4cc364e55b67138705976bab193c9"
SRCREV_machine_qemuppc ?= "257f843ea367744620f1d92910afd2f454e31483"
SRCREV_machine_qemux86 ?= "257f843ea367744620f1d92910afd2f454e31483"
SRCREV_machine_qemux86-64 ?= "257f843ea367744620f1d92910afd2f454e31483"
SRCREV_machine_qemumips64 ?= "135d85c1f8cded6b68d533a0018b2897230b880c"
SRCREV_machine ?= "257f843ea367744620f1d92910afd2f454e31483"
SRCREV_meta ?= "44a22d45cbcd7e14ea635d36949e14135f540fe0"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.19"

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
