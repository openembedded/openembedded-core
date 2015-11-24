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

SRCREV_machine_qemuarm ?= "5039adac938a19aa547f94551126a8421ce62745"
SRCREV_machine_qemuarm64 ?= "ec44c165aa1d30bde477196ee428d82530e55485"
SRCREV_machine_qemumips ?= "203b315e50e32ce39f9831b0518ef18f16f61025"
SRCREV_machine_qemuppc ?= "ec44c165aa1d30bde477196ee428d82530e55485"
SRCREV_machine_qemux86 ?= "ec44c165aa1d30bde477196ee428d82530e55485"
SRCREV_machine_qemux86-64 ?= "ec44c165aa1d30bde477196ee428d82530e55485"
SRCREV_machine_qemumips64 ?= "9223cd7daecddcbdb0818aaa6edfbd90060d1b84"
SRCREV_machine ?= "ec44c165aa1d30bde477196ee428d82530e55485"
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
