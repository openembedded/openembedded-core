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

SRCREV_machine_qemuarm ?= "bb6c714397ab4c48f4fcc76c0a609afbb42dfa2a"
SRCREV_machine_qemuarm64 ?= "f358ce2569953d18cf6bd91d0269076938e5b091"
SRCREV_machine_qemumips ?= "4a42cbc6464c592a8ce81cf9aefb780df02e10ac"
SRCREV_machine_qemuppc ?= "f0ecbfc7c5c24f0ecdd05e3304f0bea302ed116c"
SRCREV_machine_qemux86 ?= "f358ce2569953d18cf6bd91d0269076938e5b091"
SRCREV_machine_qemux86-64 ?= "f358ce2569953d18cf6bd91d0269076938e5b091"
SRCREV_machine_qemumips64 ?= "9162b0e9523407b638a3f7e2ed26450334e24969"
SRCREV_machine ?= "f358ce2569953d18cf6bd91d0269076938e5b091"
SRCREV_meta ?= "89785d2b18fa49233046125fddee8e161c8bec4d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.35"

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
