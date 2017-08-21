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

SRCREV_machine_qemuarm ?= "b7334672abf08f9dc9e277969e9266785580fa80"
SRCREV_machine_qemuarm64 ?= "f905fdd5da150ea809f847f00f3476220606c0ff"
SRCREV_machine_qemumips ?= "664fb3764bc804bfc9fd8ff582b374299e1fc016"
SRCREV_machine_qemuppc ?= "7392e713c9eb1c67e9e6f8f2d14cae44467c1fbe"
SRCREV_machine_qemux86 ?= "f905fdd5da150ea809f847f00f3476220606c0ff"
SRCREV_machine_qemux86-64 ?= "f905fdd5da150ea809f847f00f3476220606c0ff"
SRCREV_machine_qemumips64 ?= "85e973a2366e2a42d5082f5cd57852f8086502f4"
SRCREV_machine ?= "f905fdd5da150ea809f847f00f3476220606c0ff"
SRCREV_meta ?= "660468df599bbd79a287a8a703d9ca066fd6a074"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.42"

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
