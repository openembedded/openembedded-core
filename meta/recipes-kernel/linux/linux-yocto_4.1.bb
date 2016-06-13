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

SRCREV_machine_qemuarm ?= "ec626d56541042b8bcc52c47891dd8acedb326c7"
SRCREV_machine_qemuarm64 ?= "b4acdc6d1ea9474453689e5f604dd7037cbb5033"
SRCREV_machine_qemumips ?= "95866476d89898fdb61804029352af74daa4a30d"
SRCREV_machine_qemuppc ?= "b4acdc6d1ea9474453689e5f604dd7037cbb5033"
SRCREV_machine_qemux86 ?= "b4acdc6d1ea9474453689e5f604dd7037cbb5033"
SRCREV_machine_qemux86-64 ?= "b4acdc6d1ea9474453689e5f604dd7037cbb5033"
SRCREV_machine_qemumips64 ?= "9824fc2399454b9c521b0b1bde075ac862357d41"
SRCREV_machine ?= "b4acdc6d1ea9474453689e5f604dd7037cbb5033"
SRCREV_meta ?= "f0c7ef844e98c82db9ee90e20c0eff6596919ff5"

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
