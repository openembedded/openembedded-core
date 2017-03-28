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

SRCREV_machine_qemuarm ?= "01aaede0a243457a3557167d40488d89335d9e73"
SRCREV_machine_qemuarm64 ?= "2525fc844188708834929d119cecdc2f6ae3c88a"
SRCREV_machine_qemumips ?= "c7ac6707736e0e7b93d93428d805df356a9f3888"
SRCREV_machine_qemuppc ?= "2525fc844188708834929d119cecdc2f6ae3c88a"
SRCREV_machine_qemux86 ?= "2525fc844188708834929d119cecdc2f6ae3c88a"
SRCREV_machine_qemux86-64 ?= "2525fc844188708834929d119cecdc2f6ae3c88a"
SRCREV_machine_qemumips64 ?= "6af68e1545ba85755591a1495c6d34b29ac61406"
SRCREV_machine ?= "2525fc844188708834929d119cecdc2f6ae3c88a"
SRCREV_meta ?= "271b0c8d5128f0cb18ed413cc59f683132ff8e3f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.56"

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
