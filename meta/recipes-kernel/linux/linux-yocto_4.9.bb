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

SRCREV_machine_qemuarm ?= "9027d6fa2661251146c08851a13ca623bc2c1156"
SRCREV_machine_qemuarm64 ?= "6fdf2bca12625c67b64f39e08b1b4ae7c610f8bd"
SRCREV_machine_qemumips ?= "ce10acbd809335dd0c6f697770b3279ae168d145"
SRCREV_machine_qemuppc ?= "6fdf2bca12625c67b64f39e08b1b4ae7c610f8bd"
SRCREV_machine_qemux86 ?= "6fdf2bca12625c67b64f39e08b1b4ae7c610f8bd"
SRCREV_machine_qemux86-64 ?= "6fdf2bca12625c67b64f39e08b1b4ae7c610f8bd"
SRCREV_machine_qemumips64 ?= "303f51499cfe9f770c4a843a2042228897cea1b4"
SRCREV_machine ?= "6fdf2bca12625c67b64f39e08b1b4ae7c610f8bd"
SRCREV_meta ?= "2adba5d7c1a6c0425b02b1a650e7a6c320ee3acf"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.4"

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
