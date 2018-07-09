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

SRCREV_machine_qemuarm ?= "b1aa2a86605847e98799eb7a9720659639b95c35"
SRCREV_machine_qemuarm64 ?= "347393ce79e7c4aeb557e76a3255029ac35ff842"
SRCREV_machine_qemumips ?= "f2e9eb5ef808d5bf6ef00e549e7f4a297e3101fa"
SRCREV_machine_qemuppc ?= "347393ce79e7c4aeb557e76a3255029ac35ff842"
SRCREV_machine_qemux86 ?= "347393ce79e7c4aeb557e76a3255029ac35ff842"
SRCREV_machine_qemux86-64 ?= "347393ce79e7c4aeb557e76a3255029ac35ff842"
SRCREV_machine_qemumips64 ?= "f4cade13f87df43e14d340e001b553dcf9665a8e"
SRCREV_machine ?= "347393ce79e7c4aeb557e76a3255029ac35ff842"
SRCREV_meta ?= "dcef2499cc6680ce49e1a79ca371bbce403e1b93"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

DEPENDS += "openssl-native util-linux-native"

LINUX_VERSION ?= "4.12.25"

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
