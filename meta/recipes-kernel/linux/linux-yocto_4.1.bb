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

SRCREV_machine_qemuarm ?= "187e28708b478b5d01a7df9e6410a340347e4819"
SRCREV_machine_qemuarm64 ?= "52a4a9f4a2b408ef3a0745372e44cc362832d86d"
SRCREV_machine_qemumips ?= "dd6b1f0940dbffd5e0feef2beecbc89a25da8e8f"
SRCREV_machine_qemuppc ?= "52a4a9f4a2b408ef3a0745372e44cc362832d86d"
SRCREV_machine_qemux86 ?= "52a4a9f4a2b408ef3a0745372e44cc362832d86d"
SRCREV_machine_qemux86-64 ?= "52a4a9f4a2b408ef3a0745372e44cc362832d86d"
SRCREV_machine_qemumips64 ?= "8458e792e2c595f48bc5b2924e20e844f2a96522"
SRCREV_machine ?= "52a4a9f4a2b408ef3a0745372e44cc362832d86d"
SRCREV_meta ?= "3d8f1378d07dbc052ca8a7c22297339ad7998b5e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.8"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuarm=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
