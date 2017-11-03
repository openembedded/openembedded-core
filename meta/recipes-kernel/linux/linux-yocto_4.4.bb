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

SRCREV_machine_qemuarm ?= "5f3e5944108bc43f8ad657be259569b15e16b0f7"
SRCREV_machine_qemuarm64 ?= "f070447fb60b5b939c94583d7c05f55ec2b37acd"
SRCREV_machine_qemumips ?= "5827dccb88b14a64dbe6ee78efb07735236ce8ea"
SRCREV_machine_qemuppc ?= "f070447fb60b5b939c94583d7c05f55ec2b37acd"
SRCREV_machine_qemux86 ?= "f070447fb60b5b939c94583d7c05f55ec2b37acd"
SRCREV_machine_qemux86-64 ?= "f070447fb60b5b939c94583d7c05f55ec2b37acd"
SRCREV_machine_qemumips64 ?= "b8bcb7ea6836c9373f03fec69438d0c7225125f8"
SRCREV_machine ?= "f070447fb60b5b939c94583d7c05f55ec2b37acd"
SRCREV_meta ?= "ae0119a2ff737b8c14bdf904b4c6eb790a7792cb"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.93"

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
