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

SRCREV_machine_qemuarm ?= "48ae2ebeea13e8d4c405a9a6930814ddfb2faf56"
SRCREV_machine_qemuarm64 ?= "6039d7e04ee91c7fd40b1444fcca07be3fe0d5d9"
SRCREV_machine_qemumips ?= "d8ffb4a5bf10ce1deeeb1a3b33a010e86bc4ec8b"
SRCREV_machine_qemuppc ?= "6039d7e04ee91c7fd40b1444fcca07be3fe0d5d9"
SRCREV_machine_qemux86 ?= "6039d7e04ee91c7fd40b1444fcca07be3fe0d5d9"
SRCREV_machine_qemux86-64 ?= "6039d7e04ee91c7fd40b1444fcca07be3fe0d5d9"
SRCREV_machine_qemumips64 ?= "96e53c9bac505c8783fa89edb5e047e99ab7784c"
SRCREV_machine ?= "6039d7e04ee91c7fd40b1444fcca07be3fe0d5d9"
SRCREV_meta ?= "358b2bb4d2bc8e790f69ea1778d7d16184b1ae34"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.18"

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
