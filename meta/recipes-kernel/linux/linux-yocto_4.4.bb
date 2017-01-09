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

SRCREV_machine_qemuarm ?= "60287c1951ca7063bc19597eb5cabf834e660cfb"
SRCREV_machine_qemuarm64 ?= "ad8b1d659ddd2699ebf7d50ef9de8940b157bfc2"
SRCREV_machine_qemumips ?= "143d659ae6d53623001e22c13a0286bd6d1723ca"
SRCREV_machine_qemuppc ?= "ad8b1d659ddd2699ebf7d50ef9de8940b157bfc2"
SRCREV_machine_qemux86 ?= "ad8b1d659ddd2699ebf7d50ef9de8940b157bfc2"
SRCREV_machine_qemux86-64 ?= "ad8b1d659ddd2699ebf7d50ef9de8940b157bfc2"
SRCREV_machine_qemumips64 ?= "c74cc42106745bab08de6263b9320ac029f2cca9"
SRCREV_machine ?= "ad8b1d659ddd2699ebf7d50ef9de8940b157bfc2"
SRCREV_meta ?= "78a26182e20c0a49c1adda63faa15ccd3f4ecb27"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.41"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
