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

SRCREV_machine_qemuarm ?= "a261c06c3a13eec8ef48c2929a1cfdcceb5268a9"
SRCREV_machine_qemuarm64 ?= "0e30a74f2cbbab0c7014561fe4eab1c9d8bfe560"
SRCREV_machine_qemumips ?= "b4bde84e42e4b62702421a2c091072e97196e03c"
SRCREV_machine_qemuppc ?= "0e30a74f2cbbab0c7014561fe4eab1c9d8bfe560"
SRCREV_machine_qemux86 ?= "0e30a74f2cbbab0c7014561fe4eab1c9d8bfe560"
SRCREV_machine_qemux86-64 ?= "0e30a74f2cbbab0c7014561fe4eab1c9d8bfe560"
SRCREV_machine_qemumips64 ?= "14a513a5ba6a6b6968347014040d800496339622"
SRCREV_machine ?= "0e30a74f2cbbab0c7014561fe4eab1c9d8bfe560"
SRCREV_meta ?= "01ac19ede037b753d2b3f0adb20ab1becb7e1511"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.13"

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
