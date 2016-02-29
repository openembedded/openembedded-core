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

SRCREV_machine_qemuarm ?= "10de32377332ffc163525e850817780f5c577827"
SRCREV_machine_qemuarm64 ?= "abb7da90b75c0f2e0bd2f57da3ddbbe8ab670f73"
SRCREV_machine_qemumips ?= "6b8a34fc2d605ac87c4769670a8ea05804779ad0"
SRCREV_machine_qemuppc ?= "abb7da90b75c0f2e0bd2f57da3ddbbe8ab670f73"
SRCREV_machine_qemux86 ?= "abb7da90b75c0f2e0bd2f57da3ddbbe8ab670f73"
SRCREV_machine_qemux86-64 ?= "abb7da90b75c0f2e0bd2f57da3ddbbe8ab670f73"
SRCREV_machine_qemumips64 ?= "ebc3b0add55a3906616334fd4179b5dc04128276"
SRCREV_machine ?= "abb7da90b75c0f2e0bd2f57da3ddbbe8ab670f73"
SRCREV_meta ?= "4d2d541ae090e9646e6fb06ad296bdba54fd7497"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.2"

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

SRC_URI_append = " file://0001-Fix-qemux86-pat-issue.patch"
