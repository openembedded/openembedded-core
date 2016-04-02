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

SRCREV_machine_qemuarm ?= "8abee78b727ce6a4df262d1befcbecfa26742ae0"
SRCREV_machine_qemuarm64 ?= "bc22b90fb5834ead65c31ca0d1e117239d4b545d"
SRCREV_machine_qemumips ?= "475e28c3240610c26ee0b7866dc0ce701d0c27e1"
SRCREV_machine_qemuppc ?= "bc22b90fb5834ead65c31ca0d1e117239d4b545d"
SRCREV_machine_qemux86 ?= "bc22b90fb5834ead65c31ca0d1e117239d4b545d"
SRCREV_machine_qemux86-64 ?= "bc22b90fb5834ead65c31ca0d1e117239d4b545d"
SRCREV_machine_qemumips64 ?= "8d34fd84b9c53507b3d10a4a44294c479237319c"
SRCREV_machine ?= "bc22b90fb5834ead65c31ca0d1e117239d4b545d"
SRCREV_meta ?= "770996a263e22562c81f48fde0f0dc647156abce"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.3"

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
