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

SRCREV_machine_qemuarm ?= "5e6960c60bf7c107978312f7e590bdec6676ceb2"
SRCREV_machine_qemuarm64 ?= "49719d35dca0562ab3e67f37de0d4430dad68a78"
SRCREV_machine_qemumips ?= "9c00d4cf09030ff438033ae094615894f3290628"
SRCREV_machine_qemuppc ?= "49719d35dca0562ab3e67f37de0d4430dad68a78"
SRCREV_machine_qemux86 ?= "49719d35dca0562ab3e67f37de0d4430dad68a78"
SRCREV_machine_qemux86-64 ?= "49719d35dca0562ab3e67f37de0d4430dad68a78"
SRCREV_machine_qemumips64 ?= "9aaa4d26a05a1816383a1e1dbc11de5830458eec"
SRCREV_machine ?= "49719d35dca0562ab3e67f37de0d4430dad68a78"
SRCREV_meta ?= "9f68667031354532563766a3d04ca8a618e9177a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.26"

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
