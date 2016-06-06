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

SRCREV_machine_qemuarm ?= "a7b999561115b0b84814a2a27fe3f8c5b0599b0d"
SRCREV_machine_qemuarm64 ?= "57f17e7008c57885ef5c41bda2829589e499ae28"
SRCREV_machine_qemumips ?= "ce9d4e563925e65c7a4b73fd6027ed8d2a4fc5e4"
SRCREV_machine_qemuppc ?= "57f17e7008c57885ef5c41bda2829589e499ae28"
SRCREV_machine_qemux86 ?= "57f17e7008c57885ef5c41bda2829589e499ae28"
SRCREV_machine_qemux86-64 ?= "57f17e7008c57885ef5c41bda2829589e499ae28"
SRCREV_machine_qemumips64 ?= "75260c1966188a38e070420624ac65b188f77948"
SRCREV_machine ?= "57f17e7008c57885ef5c41bda2829589e499ae28"
SRCREV_meta ?= "4b4199bd24f206d459061bb0a920d009429d5ed3"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.24"

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
