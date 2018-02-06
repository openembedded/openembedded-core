KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "52cbbd4d3e216cbf0c12ae66f012103b20777884"
SRCREV_machine_qemuarm64 ?= "ae0fb21fdde3df106d7dad76620bdf39721425e2"
SRCREV_machine_qemumips ?= "900ca1bbd59fe4bf28a767e816d1883d173d7397"
SRCREV_machine_qemuppc ?= "ae0fb21fdde3df106d7dad76620bdf39721425e2"
SRCREV_machine_qemux86 ?= "ae0fb21fdde3df106d7dad76620bdf39721425e2"
SRCREV_machine_qemux86-64 ?= "ae0fb21fdde3df106d7dad76620bdf39721425e2"
SRCREV_machine_qemumips64 ?= "c6c6244d216a6c2f87c807d8e2e4bc42baa7c480"
SRCREV_machine ?= "ae0fb21fdde3df106d7dad76620bdf39721425e2"
SRCREV_meta ?= "0e964663a7978bdb459c28f3777e1b6dfe97d93d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.16"

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
