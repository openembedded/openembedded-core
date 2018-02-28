KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "481c15cd5198aa4de9faa1b0edcf69e08f45540b"
SRCREV_machine_qemuarm64 ?= "57ed1d816d318dfafd0339fade9c694b3c00998d"
SRCREV_machine_qemumips ?= "b1adde8239132a9e189acbfd0e1c27d1a4ae695f"
SRCREV_machine_qemuppc ?= "57ed1d816d318dfafd0339fade9c694b3c00998d"
SRCREV_machine_qemux86 ?= "57ed1d816d318dfafd0339fade9c694b3c00998d"
SRCREV_machine_qemux86-64 ?= "57ed1d816d318dfafd0339fade9c694b3c00998d"
SRCREV_machine_qemumips64 ?= "971d3f31a5caf6035dbff8cefea2323c196178a9"
SRCREV_machine ?= "57ed1d816d318dfafd0339fade9c694b3c00998d"
SRCREV_meta ?= "c5e05f3c61046bf6445adff30d5a03a82677d351"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.19"

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
