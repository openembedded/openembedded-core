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

SRCREV_machine_qemuarm ?= "b97dcd4f063c3d8c1d7fac555c0a189fe44b1eec"
SRCREV_machine_qemuarm64 ?= "53be19cad65e798c14be6d0365eb8f41ff38e540"
SRCREV_machine_qemumips ?= "6bebfa141f0e378673c810aaf3427168848a0b02"
SRCREV_machine_qemuppc ?= "53be19cad65e798c14be6d0365eb8f41ff38e540"
SRCREV_machine_qemux86 ?= "53be19cad65e798c14be6d0365eb8f41ff38e540"
SRCREV_machine_qemux86-64 ?= "53be19cad65e798c14be6d0365eb8f41ff38e540"
SRCREV_machine_qemumips64 ?= "0602a7509bac454c4ca4efd6fc26de4badc3d938"
SRCREV_machine ?= "53be19cad65e798c14be6d0365eb8f41ff38e540"
SRCREV_meta ?= "01f18cba44d688687cb20c6fe548743fff7f2756"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.10.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.10;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.10.5"

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
