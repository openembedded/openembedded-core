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

SRCREV_machine_qemuarm ?= "9dc59f0efe6cd1dd806b5b6089faf803db9018a4"
SRCREV_machine_qemuarm64 ?= "0b52a52fb892c0dd20823268830ab22a9e3a92b8"
SRCREV_machine_qemumips ?= "0ed887a8c24e2c2a8a2f136b9ef5031bdf6cde8c"
SRCREV_machine_qemuppc ?= "0b52a52fb892c0dd20823268830ab22a9e3a92b8"
SRCREV_machine_qemux86 ?= "0b52a52fb892c0dd20823268830ab22a9e3a92b8"
SRCREV_machine_qemux86-64 ?= "0b52a52fb892c0dd20823268830ab22a9e3a92b8"
SRCREV_machine_qemumips64 ?= "06fff8284bbec52a36fb3d054354db2f593376ac"
SRCREV_machine ?= "0b52a52fb892c0dd20823268830ab22a9e3a92b8"
SRCREV_meta ?= "20b8e96e097b796673bea982918868d47d51081b"

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
