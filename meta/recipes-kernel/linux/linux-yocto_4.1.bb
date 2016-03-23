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

SRCREV_machine_qemuarm ?= "e6b84f3f0c7292a7d3453e6bebd9987e08678a4b"
SRCREV_machine_qemuarm64 ?= "e9b00f938e9971cf84dd33f317719c91135246e9"
SRCREV_machine_qemumips ?= "c70a6ad907f0f9ffd4d911b6413e509766fba07b"
SRCREV_machine_qemuppc ?= "e9b00f938e9971cf84dd33f317719c91135246e9"
SRCREV_machine_qemux86 ?= "e9b00f938e9971cf84dd33f317719c91135246e9"
SRCREV_machine_qemux86-64 ?= "e9b00f938e9971cf84dd33f317719c91135246e9"
SRCREV_machine_qemumips64 ?= "a023db1f6f40280d3e254aed327f61f6d138992e"
SRCREV_machine ?= "e9b00f938e9971cf84dd33f317719c91135246e9"
SRCREV_meta ?= "b9023d4c8fbbb854c26f158a079a5f54dd61964d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.18"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
