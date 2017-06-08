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

SRCREV_machine_qemuarm ?= "734cdab9878ac73dbf43d8e7d14b07b9e3498c32"
SRCREV_machine_qemuarm64 ?= "d2de43e42bb8a270e680aab1c8f2447bff0b3552"
SRCREV_machine_qemumips ?= "59f4ca90a4bb47ce9c81508e3b8458b87f411d59"
SRCREV_machine_qemuppc ?= "d2de43e42bb8a270e680aab1c8f2447bff0b3552"
SRCREV_machine_qemux86 ?= "d2de43e42bb8a270e680aab1c8f2447bff0b3552"
SRCREV_machine_qemux86-64 ?= "d2de43e42bb8a270e680aab1c8f2447bff0b3552"
SRCREV_machine_qemumips64 ?= "5b396803703aa5c1bd1e6f5cc7ac02de05cd0d08"
SRCREV_machine ?= "d2de43e42bb8a270e680aab1c8f2447bff0b3552"
SRCREV_meta ?= "60dd89e2423aaa2e7be680db09be43d1f47471e0"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.31"

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
