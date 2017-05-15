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

SRCREV_machine_qemuarm ?= "ebb58e37143db15fe1b6712487f66b0d2ccb54e1"
SRCREV_machine_qemuarm64 ?= "d2c1ed3c0cbc0d8dcf429346f7ab2e96d470b504"
SRCREV_machine_qemumips ?= "c79214a4c66138478d88f9955fae35a56390f8c9"
SRCREV_machine_qemuppc ?= "d2c1ed3c0cbc0d8dcf429346f7ab2e96d470b504"
SRCREV_machine_qemux86 ?= "d2c1ed3c0cbc0d8dcf429346f7ab2e96d470b504"
SRCREV_machine_qemux86-64 ?= "d2c1ed3c0cbc0d8dcf429346f7ab2e96d470b504"
SRCREV_machine_qemumips64 ?= "229abf6f63652058db1308a0c07e59652d3e6a23"
SRCREV_machine ?= "d2c1ed3c0cbc0d8dcf429346f7ab2e96d470b504"
SRCREV_meta ?= "4d929fac3411d03f021c04b8e45d4067e433ca26"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.10.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.10;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.10.15"

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
