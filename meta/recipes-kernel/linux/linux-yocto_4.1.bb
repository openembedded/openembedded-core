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

SRCREV_machine_qemuarm ?= "c19d15710a9e0a01a83a1ac03356390bc26a01f7"
SRCREV_machine_qemuarm64 ?= "4e659b86ffcf20ece27f42a671853d5f42d52a6c"
SRCREV_machine_qemumips ?= "ca7ddd2ce6e7c3fb599512bcc31a1de23ecb589d"
SRCREV_machine_qemuppc ?= "4e659b86ffcf20ece27f42a671853d5f42d52a6c"
SRCREV_machine_qemux86 ?= "4e659b86ffcf20ece27f42a671853d5f42d52a6c"
SRCREV_machine_qemux86-64 ?= "4e659b86ffcf20ece27f42a671853d5f42d52a6c"
SRCREV_machine_qemumips64 ?= "5945b7d7abce3ede46ae133a24420751d99ea55b"
SRCREV_machine ?= "4e659b86ffcf20ece27f42a671853d5f42d52a6c"
SRCREV_meta ?= "46bb64d605fd336d99fa05bab566b9553b40b4b4"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.13"

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
