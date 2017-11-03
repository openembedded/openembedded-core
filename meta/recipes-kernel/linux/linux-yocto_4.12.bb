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

SRCREV_machine_qemuarm ?= "03489013c6844bf3f911e51c5b8cd3bd5c1dc6e9"
SRCREV_machine_qemuarm64 ?= "9cc6b0ae1aad7312e85ac4134398f81c0140de33"
SRCREV_machine_qemumips ?= "c22264e2e3340eb8affa9a9fc0b86decb2be26c6"
SRCREV_machine_qemuppc ?= "9cc6b0ae1aad7312e85ac4134398f81c0140de33"
SRCREV_machine_qemux86 ?= "9cc6b0ae1aad7312e85ac4134398f81c0140de33"
SRCREV_machine_qemux86-64 ?= "9cc6b0ae1aad7312e85ac4134398f81c0140de33"
SRCREV_machine_qemumips64 ?= "ead3f1c16e85d1b1074acdd13084b5cdf1104e23"
SRCREV_machine ?= "9cc6b0ae1aad7312e85ac4134398f81c0140de33"
SRCREV_meta ?= "cebe198870d781829bd997a188cc34d9f7a61023"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.14"

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
