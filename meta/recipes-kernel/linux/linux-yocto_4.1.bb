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

SRCREV_machine_qemuarm ?= "f100a3d840596ac0dc47416336f34d8bf7b5ffa8"
SRCREV_machine_qemuarm64 ?= "e2bea2e96fcbbbb9b1efc3bc7505ac42e39bb5c0"
SRCREV_machine_qemumips ?= "4005321dfa890df693c12e902e48fd0dde88fee7"
SRCREV_machine_qemuppc ?= "1e29490accfb0547e3a854ac6ec9a6754cd47204"
SRCREV_machine_qemux86 ?= "e2bea2e96fcbbbb9b1efc3bc7505ac42e39bb5c0"
SRCREV_machine_qemux86-64 ?= "e2bea2e96fcbbbb9b1efc3bc7505ac42e39bb5c0"
SRCREV_machine_qemumips64 ?= "87c0f4d5bec646565e493d39bf57c3500151ed68"
SRCREV_machine ?= "e2bea2e96fcbbbb9b1efc3bc7505ac42e39bb5c0"
SRCREV_meta ?= "9f9c9a66ef3452343586adf150137967e955d71a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.42"

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
