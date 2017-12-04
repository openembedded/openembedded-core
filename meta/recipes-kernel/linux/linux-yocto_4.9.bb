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

SRCREV_machine_qemuarm ?= "a87b6d44fdde992b83a476cf9f1ad2772ebb7da0"
SRCREV_machine_qemuarm64 ?= "d8520539587665c19a0afc97858069184574da66"
SRCREV_machine_qemumips ?= "38ee2d051cba9ff9a045e67d9ebf2f9516fdcf47"
SRCREV_machine_qemuppc ?= "d8520539587665c19a0afc97858069184574da66"
SRCREV_machine_qemux86 ?= "d8520539587665c19a0afc97858069184574da66"
SRCREV_machine_qemux86-64 ?= "d8520539587665c19a0afc97858069184574da66"
SRCREV_machine_qemumips64 ?= "6219773456cdce10ab481da6718f715fc3e30ba7"
SRCREV_machine ?= "d8520539587665c19a0afc97858069184574da66"
SRCREV_meta ?= "f4e37e151102d89c4d0e110c88eb3b3c36bdeaa4"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.61"

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
