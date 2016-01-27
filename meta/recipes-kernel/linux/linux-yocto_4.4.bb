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

SRCREV_machine_qemuarm ?= "f7bc1fb5f438f019bcd3d5fd8362e0960ed0fffc"
SRCREV_machine_qemuarm64 ?= "57af322eecf5750f8f09cb8b093d613caede5c48"
SRCREV_machine_qemumips ?= "5b05677721ce8b0625a7e155dcdf93325fc460fe"
SRCREV_machine_qemuppc ?= "57af322eecf5750f8f09cb8b093d613caede5c48"
SRCREV_machine_qemux86 ?= "57af322eecf5750f8f09cb8b093d613caede5c48"
SRCREV_machine_qemux86-64 ?= "57af322eecf5750f8f09cb8b093d613caede5c48"
SRCREV_machine_qemumips64 ?= "ac3f6e9508fb6963e9db844fe28b7a50589decc4"
SRCREV_machine ?= "57af322eecf5750f8f09cb8b093d613caede5c48"
SRCREV_meta ?= "dc760c3189ed868dc59050206b7899c35a4ad8e8"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4"

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
