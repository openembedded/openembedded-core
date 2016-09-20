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

SRCREV_machine_qemuarm ?= "ba792fae8c979ae4ecaae5e8403b150863173509"
SRCREV_machine_qemuarm64 ?= "eb9c19c72d40a4bb8a6a3b7b949e41e5c31c0d94"
SRCREV_machine_qemumips ?= "19b41be93a8fefc65ef4c1c1ab3e8b2d815de93d"
SRCREV_machine_qemuppc ?= "eb9c19c72d40a4bb8a6a3b7b949e41e5c31c0d94"
SRCREV_machine_qemux86 ?= "eb9c19c72d40a4bb8a6a3b7b949e41e5c31c0d94"
SRCREV_machine_qemux86-64 ?= "eb9c19c72d40a4bb8a6a3b7b949e41e5c31c0d94"
SRCREV_machine_qemumips64 ?= "c6e9e8d1caacea5deca7e41b47fd6076a747b6fa"
SRCREV_machine ?= "eb9c19c72d40a4bb8a6a3b7b949e41e5c31c0d94"
SRCREV_meta ?= "fb4159467f0f46f788b59f0aff31e3fcd757d00d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8-rc7"

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
