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

SRCREV_machine_qemuarm ?= "9a91ef47f85a741d842b876267b29f8ded751f68"
SRCREV_machine_qemuarm64 ?= "9e7ee5155e3466824abe94eebb641c63ab5cd5f2"
SRCREV_machine_qemumips ?= "40c7a721efe2822fdb7df4b2902a1994add40a77"
SRCREV_machine_qemuppc ?= "9e7ee5155e3466824abe94eebb641c63ab5cd5f2"
SRCREV_machine_qemux86 ?= "9e7ee5155e3466824abe94eebb641c63ab5cd5f2"
SRCREV_machine_qemux86-64 ?= "9e7ee5155e3466824abe94eebb641c63ab5cd5f2"
SRCREV_machine_qemumips64 ?= "fe4805532e149324939bf9d8415ed43dac357709"
SRCREV_machine ?= "9e7ee5155e3466824abe94eebb641c63ab5cd5f2"
SRCREV_meta ?= "25fb74eaaef249519f25e243e7f9bf0cab0e1781"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8-rc5"

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
