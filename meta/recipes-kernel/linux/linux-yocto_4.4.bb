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

SRCREV_machine_qemuarm ?= "d0dae8a98254e7ddd8d9be107e33c3a492bccdae"
SRCREV_machine_qemuarm64 ?= "db28bb1e7a682362f58cc42fbb679022df9657a7"
SRCREV_machine_qemumips ?= "9024e9c842557e258a88470f8b1a772df18d9813"
SRCREV_machine_qemuppc ?= "db28bb1e7a682362f58cc42fbb679022df9657a7"
SRCREV_machine_qemux86 ?= "db28bb1e7a682362f58cc42fbb679022df9657a7"
SRCREV_machine_qemux86-64 ?= "db28bb1e7a682362f58cc42fbb679022df9657a7"
SRCREV_machine_qemumips64 ?= "88fb42ec863d20ccde57dd35da06d6221cfb4c03"
SRCREV_machine ?= "db28bb1e7a682362f58cc42fbb679022df9657a7"
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
