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

SRCREV_machine_qemuarm ?= "fae04a40c8bcdf27b237d7d288ddcad71976a7b3"
SRCREV_machine_qemuarm64 ?= "0194c765861157b95de80fa7c27ebb6b51c16dd6"
SRCREV_machine_qemumips ?= "beedcf740b175cdd0ecc7a8c473c0b518f329c33"
SRCREV_machine_qemuppc ?= "0194c765861157b95de80fa7c27ebb6b51c16dd6"
SRCREV_machine_qemux86 ?= "0194c765861157b95de80fa7c27ebb6b51c16dd6"
SRCREV_machine_qemux86-64 ?= "0194c765861157b95de80fa7c27ebb6b51c16dd6"
SRCREV_machine_qemumips64 ?= "25c3d2617df1947b3d69e480e88ba75881c7ca71"
SRCREV_machine ?= "0194c765861157b95de80fa7c27ebb6b51c16dd6"
SRCREV_meta ?= "4940c6e551c1eea41a5dbc69a90b23d5f835fa5b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.1"

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

SRC_URI_append = " file://0001-Fix-qemux86-pat-issue.patch"
