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

SRCREV_machine_qemuarm ?= "c0123490bb9b454a43c3573e2573b491e0c09701"
SRCREV_machine_qemuarm64 ?= "304f750aa5be0bf66f70720d68f16865d912ff9f"
SRCREV_machine_qemumips ?= "aa15b7a291ca18d08fd640932efad673f6b278c3"
SRCREV_machine_qemuppc ?= "d2c3ea488fe179898ae853cb9565cb7ac62291c6"
SRCREV_machine_qemux86 ?= "c50f695341260d4757af64d809390610aae213e1"
SRCREV_machine_qemux86-64 ?= "c50f695341260d4757af64d809390610aae213e1"
SRCREV_machine_qemumips64 ?= "40a880b0f21aa09f3b52d68969339d80f9cc10ad"
SRCREV_machine ?= "c50f695341260d4757af64d809390610aae213e1"
SRCREV_meta ?= "c19322afb12490d9f5ad0182339bff2fe13e778b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.18"

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
