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

SRCREV_machine_qemuarm ?= "96b21aab2adf2c9396f8cf579420a2e497923eae"
SRCREV_machine_qemuarm64 ?= "ec18b0b3bd6befd416078e81d775dab37b3f9124"
SRCREV_machine_qemumips ?= "e0a6ffb62a4d28659e0e1be7b1a42a8ef499aa3b"
SRCREV_machine_qemuppc ?= "ec18b0b3bd6befd416078e81d775dab37b3f9124"
SRCREV_machine_qemux86 ?= "ec18b0b3bd6befd416078e81d775dab37b3f9124"
SRCREV_machine_qemux86-64 ?= "ec18b0b3bd6befd416078e81d775dab37b3f9124"
SRCREV_machine_qemumips64 ?= "1b1349ca5a642086d4b92347fb9d1de1c6eab742"
SRCREV_machine ?= "ec18b0b3bd6befd416078e81d775dab37b3f9124"
SRCREV_meta ?= "56dcb623ebf5e83d65fdb4eb270f23676bb000a5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.18"

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
