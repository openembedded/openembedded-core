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

SRCREV_machine_qemuarm ?= "df8f2bd306f5e22590991faee46e28f671fa19b3"
SRCREV_machine_qemuarm64 ?= "9f166e918f63dd7214ad0388d64709d33f2a14a3"
SRCREV_machine_qemumips ?= "1bb60e693b913dacad698bec1cc08b350785e3d1"
SRCREV_machine_qemuppc ?= "9f166e918f63dd7214ad0388d64709d33f2a14a3"
SRCREV_machine_qemux86 ?= "9f166e918f63dd7214ad0388d64709d33f2a14a3"
SRCREV_machine_qemux86-64 ?= "9f166e918f63dd7214ad0388d64709d33f2a14a3"
SRCREV_machine_qemumips64 ?= "b0e7bb69566a922a661c4902e496dce98cefeab9"
SRCREV_machine ?= "9f166e918f63dd7214ad0388d64709d33f2a14a3"
SRCREV_meta ?= "51216d70967be79dbe6c75ed4c7979abd4b95272"

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
