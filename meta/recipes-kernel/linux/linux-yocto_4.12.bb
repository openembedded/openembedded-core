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

SRCREV_machine_qemuarm ?= "d28b66327c41ded21f49bd1b4bf35e8d3a5684c3"
SRCREV_machine_qemuarm64 ?= "42a33c00cd35d9de9566488bcedd01d9c6b0ab03"
SRCREV_machine_qemumips ?= "2e6d1d8f4d63400aac07dcadfd94c1337d4b7e2f"
SRCREV_machine_qemuppc ?= "42a33c00cd35d9de9566488bcedd01d9c6b0ab03"
SRCREV_machine_qemux86 ?= "42a33c00cd35d9de9566488bcedd01d9c6b0ab03"
SRCREV_machine_qemux86-64 ?= "42a33c00cd35d9de9566488bcedd01d9c6b0ab03"
SRCREV_machine_qemumips64 ?= "5ad067fb6b042cc68252ba746a244d7a97be75fc"
SRCREV_machine ?= "42a33c00cd35d9de9566488bcedd01d9c6b0ab03"
SRCREV_meta ?= "146a863c246f803a074a5abc12e69737bc6751b2"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.16"

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
