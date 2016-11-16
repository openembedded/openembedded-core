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

SRCREV_machine_qemuarm ?= "800a8564447b6ec75500b7cd6ebce78d652a466e"
SRCREV_machine_qemuarm64 ?= "2e7e9f1e599eb255d038ede09f1765a1bb1378cf"
SRCREV_machine_qemumips ?= "e01c5adddc9e48bb2b3ae74ae5f12f356a6519dd"
SRCREV_machine_qemuppc ?= "2e7e9f1e599eb255d038ede09f1765a1bb1378cf"
SRCREV_machine_qemux86 ?= "6745463c22f59717df34f819359ea2955611fd03"
SRCREV_machine_qemux86-64 ?= "6745463c22f59717df34f819359ea2955611fd03"
SRCREV_machine_qemumips64 ?= "b9217c2fa88d138f4082c730fb3b450f7c56da81"
SRCREV_machine ?= "6745463c22f59717df34f819359ea2955611fd03"
SRCREV_meta ?= "1eac0b697a507e2dfeeb06294c887425170711b9"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.8"

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
