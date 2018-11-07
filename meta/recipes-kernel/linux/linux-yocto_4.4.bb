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

SRCREV_machine_qemuarm ?= "a68a73dbd3c37ec21239dd97060eef308f1ff958"
SRCREV_machine_qemuarm64 ?= "a575843cceb539c7b0514e7d74b7936ca104b623"
SRCREV_machine_qemumips ?= "3c0e62ea8803a1757e389dcd6233e3d6acba8d2c"
SRCREV_machine_qemuppc ?= "a575843cceb539c7b0514e7d74b7936ca104b623"
SRCREV_machine_qemux86 ?= "a575843cceb539c7b0514e7d74b7936ca104b623"
SRCREV_machine_qemux86-64 ?= "a575843cceb539c7b0514e7d74b7936ca104b623"
SRCREV_machine_qemumips64 ?= "eaed2a94a20c7f65afa342d9243f19337f63b434"
SRCREV_machine ?= "a575843cceb539c7b0514e7d74b7936ca104b623"
SRCREV_meta ?= "69ebea34250696ebe2d8c87c553480974e56d922"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.162"

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
