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

SRCREV_machine_qemuarm ?= "16f32b5197fbaec1dbf4f380015792ed47322f06"
SRCREV_machine_qemuarm64 ?= "38631cc4a8f89fb9547af9e44213971a5b360a36"
SRCREV_machine_qemumips ?= "bcbe7a7845e3061998a740f12e88a792f358393e"
SRCREV_machine_qemuppc ?= "4858eec374ee4b09e4428895141ad1ed26fb6d64"
SRCREV_machine_qemux86 ?= "9bcb4ea3fa107f1a8790c8c3408eb250e8d1d66e"
SRCREV_machine_qemux86-64 ?= "9bcb4ea3fa107f1a8790c8c3408eb250e8d1d66e"
SRCREV_machine_qemumips64 ?= "b90b544d7a5b004b635cf337b29e15294cb17553"
SRCREV_machine ?= "9bcb4ea3fa107f1a8790c8c3408eb250e8d1d66e"
SRCREV_meta ?= "3edb4de355873d32da9307a011adea2542bd05a7"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.17"

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
