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

SRCREV_machine_qemuarm ?= "09b1056330a9a4fc077d569513c2b40e8340e813"
SRCREV_machine_qemuarm64 ?= "881cba95ae2597482870f6c4060a60d84af4c5d1"
SRCREV_machine_qemumips ?= "858b4f40b3c2dbb6cb2ac76cc27dd5275d83f1f9"
SRCREV_machine_qemuppc ?= "881cba95ae2597482870f6c4060a60d84af4c5d1"
SRCREV_machine_qemux86 ?= "881cba95ae2597482870f6c4060a60d84af4c5d1"
SRCREV_machine_qemux86-64 ?= "881cba95ae2597482870f6c4060a60d84af4c5d1"
SRCREV_machine_qemumips64 ?= "32baa0c2e903fdfa714c38428ba055ad861d058d"
SRCREV_machine ?= "881cba95ae2597482870f6c4060a60d84af4c5d1"
SRCREV_meta ?= "03a2d3f7f999a555c8725b5f1fd69660ebd4af83"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.3"

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
