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

SRCREV_machine_qemuarm ?= "06702ee20de46795634c3f4e6497a73935856c99"
SRCREV_machine_qemuarm64 ?= "62acc21a6d4492d649fd6361c9cd18b9624845c6"
SRCREV_machine_qemumips ?= "b7cb5132e86d30640da745b8c36727291e7109e6"
SRCREV_machine_qemuppc ?= "62acc21a6d4492d649fd6361c9cd18b9624845c6"
SRCREV_machine_qemux86 ?= "62acc21a6d4492d649fd6361c9cd18b9624845c6"
SRCREV_machine_qemux86-64 ?= "62acc21a6d4492d649fd6361c9cd18b9624845c6"
SRCREV_machine_qemumips64 ?= "53c911666987c77a3171db8291d4b6df83836620"
SRCREV_machine ?= "62acc21a6d4492d649fd6361c9cd18b9624845c6"
SRCREV_meta ?= "e775f4ed1e1358ce0647751e69cc54aa9d5d5d06"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.14"

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
