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

SRCREV_machine_qemuarm ?= "6b5ee65b1f353c9b6a61402bfa6919f8f7a1969b"
SRCREV_machine_qemuarm64 ?= "24e2e14aa446f6e4e127f2a0a004a72c430f337e"
SRCREV_machine_qemumips ?= "fb0ec85bc42dc0893440e825cbdb709c15bc1704"
SRCREV_machine_qemuppc ?= "24e2e14aa446f6e4e127f2a0a004a72c430f337e"
SRCREV_machine_qemux86 ?= "24e2e14aa446f6e4e127f2a0a004a72c430f337e"
SRCREV_machine_qemux86-64 ?= "24e2e14aa446f6e4e127f2a0a004a72c430f337e"
SRCREV_machine_qemumips64 ?= "b9ee3a1ff68fbb978864203735b4d0e200ee50f4"
SRCREV_machine ?= "24e2e14aa446f6e4e127f2a0a004a72c430f337e"
SRCREV_meta ?= "b9023d4c8fbbb854c26f158a079a5f54dd61964d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.18"

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
