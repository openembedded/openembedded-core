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

SRCREV_machine_qemuarm ?= "f217a40e5b17deb87ed3859b29b1c355e3bb4abc"
SRCREV_machine_qemuarm64 ?= "15cf090ded5157e67302313bff9da0fa056e8ea9"
SRCREV_machine_qemumips ?= "7f1ea2fe2cf42f117c37cd174236e07eba576382"
SRCREV_machine_qemuppc ?= "15cf090ded5157e67302313bff9da0fa056e8ea9"
SRCREV_machine_qemux86 ?= "15cf090ded5157e67302313bff9da0fa056e8ea9"
SRCREV_machine_qemux86-64 ?= "15cf090ded5157e67302313bff9da0fa056e8ea9"
SRCREV_machine_qemumips64 ?= "7d7a10a3c4465eca0e0879b3596da9b013ed3bb8"
SRCREV_machine ?= "15cf090ded5157e67302313bff9da0fa056e8ea9"
SRCREV_meta ?= "cab4fec4b7ab0efae0f44c1ec1836c035a9b78fe"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.27"

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
