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

SRCREV_machine_qemuarm ?= "0ec64d1dadd3de6736b5b9acd5ffc7b3b9a22071"
SRCREV_machine_qemuarm64 ?= "61d2bedf25deac15c19413ee1b057067657d97d3"
SRCREV_machine_qemumips ?= "8d5c7074354cef47bbc2e3aabd926a3409f02b12"
SRCREV_machine_qemuppc ?= "11c828d1e438aba1be93c4e19cb458f6391c3aa6"
SRCREV_machine_qemux86 ?= "61d2bedf25deac15c19413ee1b057067657d97d3"
SRCREV_machine_qemux86-64 ?= "61d2bedf25deac15c19413ee1b057067657d97d3"
SRCREV_machine_qemumips64 ?= "03a03f3543a050b8a83618ed25d408beaa099371"
SRCREV_machine ?= "61d2bedf25deac15c19413ee1b057067657d97d3"
SRCREV_meta ?= "4de9b8f96c8dca0c55a496792a2ad4d2776e6657"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.37"

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
