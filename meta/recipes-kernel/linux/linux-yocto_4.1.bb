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

SRCREV_machine_qemuarm ?= "5f2bc8d122ac2247d5a01025f73d09ae723efb21"
SRCREV_machine_qemuarm64 ?= "a5327464dad92042f79dd5fb12523d16780980fb"
SRCREV_machine_qemumips ?= "6e4bc756e0289b9f1869bfb5d84f033df2437d0b"
SRCREV_machine_qemuppc ?= "a5327464dad92042f79dd5fb12523d16780980fb"
SRCREV_machine_qemux86 ?= "a5327464dad92042f79dd5fb12523d16780980fb"
SRCREV_machine_qemux86-64 ?= "a5327464dad92042f79dd5fb12523d16780980fb"
SRCREV_machine_qemumips64 ?= "e83d6e6fcfc04591109e470e55532d25a2a1bef1"
SRCREV_machine ?= "a5327464dad92042f79dd5fb12523d16780980fb"
SRCREV_meta ?= "79dbb64d9e179718369a7a5c7b364fda9936571f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.17"

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
