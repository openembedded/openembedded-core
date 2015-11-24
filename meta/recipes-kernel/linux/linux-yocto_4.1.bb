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

SRCREV_machine_qemuarm ?= "817a421e7c1be9c1136da5772f351e77513a1ed2"
SRCREV_machine_qemuarm64 ?= "c22dc676d6d560d0b2a316283a514d65d2a510e4"
SRCREV_machine_qemumips ?= "9f244d964a5910244fc82c261c48e1dc0862654b"
SRCREV_machine_qemuppc ?= "c22dc676d6d560d0b2a316283a514d65d2a510e4"
SRCREV_machine_qemux86 ?= "c22dc676d6d560d0b2a316283a514d65d2a510e4"
SRCREV_machine_qemux86-64 ?= "c22dc676d6d560d0b2a316283a514d65d2a510e4"
SRCREV_machine_qemumips64 ?= "7fdfaf2387d7896c1ead6a000616e01ce72f0764"
SRCREV_machine ?= "c22dc676d6d560d0b2a316283a514d65d2a510e4"
SRCREV_meta ?= "30c4b4bd79c5d774de6dcf23d0deab554e31f3d4"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.13"

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
