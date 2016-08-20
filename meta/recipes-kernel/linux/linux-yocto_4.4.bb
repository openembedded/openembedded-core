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

SRCREV_machine_qemuarm ?= "81cae0bb962c278b7fd616ce94b0a1ea21b9a938"
SRCREV_machine_qemuarm64 ?= "0a0c93f29c0d65c00abdd2f6d1eb89134fae9525"
SRCREV_machine_qemumips ?= "92bae966ed0bdf4bd8c8343978f83b80b8fa9981"
SRCREV_machine_qemuppc ?= "0a0c93f29c0d65c00abdd2f6d1eb89134fae9525"
SRCREV_machine_qemux86 ?= "0a0c93f29c0d65c00abdd2f6d1eb89134fae9525"
SRCREV_machine_qemux86-64 ?= "0a0c93f29c0d65c00abdd2f6d1eb89134fae9525"
SRCREV_machine_qemumips64 ?= "aa125473aef74e7731b14f6b56c1b50589fcd668"
SRCREV_machine ?= "0a0c93f29c0d65c00abdd2f6d1eb89134fae9525"
SRCREV_meta ?= "dab902b4cfa932810bb1dc6de003ed668f935729"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.18"

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
