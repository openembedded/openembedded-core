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

SRCREV_machine_qemuarm ?= "a064c27e528be59412a892f3ed3ef8efe1f1d601"
SRCREV_machine_qemuarm64 ?= "7d1401a0dd9bebfe49937ca7d9785972e0cc76d0"
SRCREV_machine_qemumips ?= "d644de0138c623c2b5e8a6a143a24234fd2cfb27"
SRCREV_machine_qemuppc ?= "7d1401a0dd9bebfe49937ca7d9785972e0cc76d0"
SRCREV_machine_qemux86 ?= "7d1401a0dd9bebfe49937ca7d9785972e0cc76d0"
SRCREV_machine_qemux86-64 ?= "7d1401a0dd9bebfe49937ca7d9785972e0cc76d0"
SRCREV_machine_qemumips64 ?= "1fb08e61877b56fcbf800bfc66305b7a563c5f99"
SRCREV_machine ?= "7d1401a0dd9bebfe49937ca7d9785972e0cc76d0"
SRCREV_meta ?= "e66032e2d93da24c6b9137dbbe66008c77f6d4aa"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.20"

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
