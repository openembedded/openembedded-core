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

SRCREV_machine_qemuarm ?= "97c21d0b5ffa8eb1cb27a6f81b52ec53182ae087"
SRCREV_machine_qemuarm64 ?= "628bf627561c6285d99fb978e11d4c15fc29324b"
SRCREV_machine_qemumips ?= "8763860e4da46b3cb1551e09d9ca46eedd2dd91a"
SRCREV_machine_qemuppc ?= "628bf627561c6285d99fb978e11d4c15fc29324b"
SRCREV_machine_qemux86 ?= "628bf627561c6285d99fb978e11d4c15fc29324b"
SRCREV_machine_qemux86-64 ?= "628bf627561c6285d99fb978e11d4c15fc29324b"
SRCREV_machine_qemumips64 ?= "8974d9741cbcb21bf50ab49a6257e5773415fa5f"
SRCREV_machine ?= "628bf627561c6285d99fb978e11d4c15fc29324b"
SRCREV_meta ?= "6ec93aaa70f838b551f58b91d0c6ffeff6f6094b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.11"

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
