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

SRCREV_machine_qemuarm ?= "68df4c9ef9dcc59836e9e0a932bca9a4e6c201b7"
SRCREV_machine_qemuarm64 ?= "95c0a80ee83f1cf8e59d733f36e8a9dfd50a0098"
SRCREV_machine_qemumips ?= "864b3dc5e39eaf1a6b8665894ef4ff34396d8704"
SRCREV_machine_qemuppc ?= "95c0a80ee83f1cf8e59d733f36e8a9dfd50a0098"
SRCREV_machine_qemux86 ?= "95c0a80ee83f1cf8e59d733f36e8a9dfd50a0098"
SRCREV_machine_qemux86-64 ?= "95c0a80ee83f1cf8e59d733f36e8a9dfd50a0098"
SRCREV_machine_qemumips64 ?= "ac72601920adbc163cd691fa060177137e41bf22"
SRCREV_machine ?= "95c0a80ee83f1cf8e59d733f36e8a9dfd50a0098"
SRCREV_meta ?= "38e74b65164e6857450417825ee4b517452ff57f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.13"

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
