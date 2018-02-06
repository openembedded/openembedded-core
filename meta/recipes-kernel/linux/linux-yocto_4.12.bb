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

SRCREV_machine_qemuarm ?= "77d25f21501511f01eb3df3c58dd8cf10a90adce"
SRCREV_machine_qemuarm64 ?= "4b7a5c1b4ec5536806942340755bcfbf6f3584d9"
SRCREV_machine_qemumips ?= "567ea03f6c5844bed62957b20747b4315851e3c3"
SRCREV_machine_qemuppc ?= "4b7a5c1b4ec5536806942340755bcfbf6f3584d9"
SRCREV_machine_qemux86 ?= "4b7a5c1b4ec5536806942340755bcfbf6f3584d9"
SRCREV_machine_qemux86-64 ?= "4b7a5c1b4ec5536806942340755bcfbf6f3584d9"
SRCREV_machine_qemumips64 ?= "4796b8299a405d1428f000cc4a2e8800a3670541"
SRCREV_machine ?= "4b7a5c1b4ec5536806942340755bcfbf6f3584d9"
SRCREV_meta ?= "7986844d9de597218a23f82e68b01e5bf060d576"

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
