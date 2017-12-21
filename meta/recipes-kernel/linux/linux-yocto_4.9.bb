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

SRCREV_machine_qemuarm ?= "d4807f91ce5fd40b64e11c96fb83a27b99758175"
SRCREV_machine_qemuarm64 ?= "5174e51fa08d0942ddcf8b19540e90ec10eeb621"
SRCREV_machine_qemumips ?= "27a5a33ab1bbb0838d2ccd446b1e29d797811d7b"
SRCREV_machine_qemuppc ?= "5174e51fa08d0942ddcf8b19540e90ec10eeb621"
SRCREV_machine_qemux86 ?= "5174e51fa08d0942ddcf8b19540e90ec10eeb621"
SRCREV_machine_qemux86-64 ?= "5174e51fa08d0942ddcf8b19540e90ec10eeb621"
SRCREV_machine_qemumips64 ?= "db1b582e665b95ddf87b75acce3a42660289d4ca"
SRCREV_machine ?= "5174e51fa08d0942ddcf8b19540e90ec10eeb621"
SRCREV_meta ?= "4553798a3e73b0791f4d5065ec5ad4b45027914f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.65"

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
