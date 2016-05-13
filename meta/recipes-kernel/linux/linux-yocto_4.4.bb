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

SRCREV_machine_qemuarm ?= "1d343b327d97843de4a32fda9015bc87a46dea9b"
SRCREV_machine_qemuarm64 ?= "578ff2a88676d20439dbf3877768370d06a22d8f"
SRCREV_machine_qemumips ?= "f89dd8336dcb0b7bf0291aee7ee33531564ea3b5"
SRCREV_machine_qemuppc ?= "578ff2a88676d20439dbf3877768370d06a22d8f"
SRCREV_machine_qemux86 ?= "578ff2a88676d20439dbf3877768370d06a22d8f"
SRCREV_machine_qemux86-64 ?= "578ff2a88676d20439dbf3877768370d06a22d8f"
SRCREV_machine_qemumips64 ?= "5faa3c8de491435842024b24b6a95fccdbe3f72a"
SRCREV_machine ?= "578ff2a88676d20439dbf3877768370d06a22d8f"
SRCREV_meta ?= "d6ee402d461048cf1afd10375fee5769c06d21d6"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.10"

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
