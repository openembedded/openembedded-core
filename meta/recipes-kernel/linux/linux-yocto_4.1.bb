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

SRCREV_machine_qemuarm ?= "ecabd4f1cf9b1e6279943c8e93eff58095296fb6"
SRCREV_machine_qemuarm64 ?= "96c0e80f4007cce8d4f559a23f7a22da156ccbe2"
SRCREV_machine_qemumips ?= "8fd9efe95598ddf89ec432a967a38f02b2906a8b"
SRCREV_machine_qemuppc ?= "96c0e80f4007cce8d4f559a23f7a22da156ccbe2"
SRCREV_machine_qemux86 ?= "96c0e80f4007cce8d4f559a23f7a22da156ccbe2"
SRCREV_machine_qemux86-64 ?= "96c0e80f4007cce8d4f559a23f7a22da156ccbe2"
SRCREV_machine_qemumips64 ?= "cb16b5365e26e106d56a6f8ed23eb8b57905ecf8"
SRCREV_machine ?= "96c0e80f4007cce8d4f559a23f7a22da156ccbe2"
SRCREV_meta ?= "b9023d4c8fbbb854c26f158a079a5f54dd61964d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.18"

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
