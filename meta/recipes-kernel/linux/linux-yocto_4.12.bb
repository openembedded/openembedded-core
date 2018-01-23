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

SRCREV_machine_qemuarm ?= "02385ff70983375d3c8a4172ecded3796b266955"
SRCREV_machine_qemuarm64 ?= "850696176565bc889670b96f0e27ab8166a8cf02"
SRCREV_machine_qemumips ?= "508b8f61dd8477b2af91863bfb21e119301362d2"
SRCREV_machine_qemuppc ?= "850696176565bc889670b96f0e27ab8166a8cf02"
SRCREV_machine_qemux86 ?= "850696176565bc889670b96f0e27ab8166a8cf02"
SRCREV_machine_qemux86-64 ?= "850696176565bc889670b96f0e27ab8166a8cf02"
SRCREV_machine_qemumips64 ?= "6b2b8d3be7bd298684ed2a597e24360c025201ef"
SRCREV_machine ?= "850696176565bc889670b96f0e27ab8166a8cf02"
SRCREV_meta ?= "2a4e862a6ad55e5ee8beb62f3ea53f4b77c6f4c1"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.18"

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
