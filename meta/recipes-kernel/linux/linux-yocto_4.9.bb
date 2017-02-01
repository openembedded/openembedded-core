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

SRCREV_machine_qemuarm ?= "103383d270939399bc73365c31cd6f93c567114f"
SRCREV_machine_qemuarm64 ?= "4700f2f8b9dbaad5ae441b682d04b09e811135fc"
SRCREV_machine_qemumips ?= "c8425cad333ec85f61d63054051fe17df53ece29"
SRCREV_machine_qemuppc ?= "4700f2f8b9dbaad5ae441b682d04b09e811135fc"
SRCREV_machine_qemux86 ?= "4700f2f8b9dbaad5ae441b682d04b09e811135fc"
SRCREV_machine_qemux86-64 ?= "4700f2f8b9dbaad5ae441b682d04b09e811135fc"
SRCREV_machine_qemumips64 ?= "42fe7d605fc92985c8ba10363de487ea06bd068a"
SRCREV_machine ?= "4700f2f8b9dbaad5ae441b682d04b09e811135fc"
SRCREV_meta ?= "6fd9dcbb3f0becf90c555a1740d21d18c331af99"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.6"

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
