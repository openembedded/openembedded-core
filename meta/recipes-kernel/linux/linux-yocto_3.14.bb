KBRANCH ?= "standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "standard/qemuarm64"
KBRANCH_qemumips ?= "standard/mti-malta32"
KBRANCH_qemuppc  ?= "standard/qemuppc"
KBRANCH_qemux86  ?= "standard/common-pc/base"
KBRANCH_qemux86-64 ?= "standard/common-pc-64/base"
KBRANCH_qemumips64 ?= "standard/mti-malta64"

SRCREV_machine_qemuarm ?= "9d0a4947dc7117e393dc3b8c07246d26e22ecdec"
SRCREV_machine_qemuarm64 ?= "302ca233332fd364ecd028a0cf21b4cdc045e056"
SRCREV_machine_qemumips ?= "9c6fdae47597756042a1d4ed613dff96e430e8e9"
SRCREV_machine_qemuppc ?= "e4847afbb42583fa05e6a94c8d0c8f8e37ed5622"
SRCREV_machine_qemux86 ?= "550f5379bc4b001f656e7c98165606e543d0858c"
SRCREV_machine_qemux86-64 ?= "302ca233332fd364ecd028a0cf21b4cdc045e056"
SRCREV_machine_qemumips64 ?= "9b203f1aff41f7b713ec973cdcc7d016db4ffc26"
SRCREV_machine ?= "302ca233332fd364ecd028a0cf21b4cdc045e056"
SRCREV_meta ?= "3a09b38a9f5015c56d99d17aa7c2f200c566249b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;branch=${KBRANCH};name=machine; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-3.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "3.14.36"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
