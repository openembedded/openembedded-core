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

SRCREV_machine_qemuarm ?= "52340eea58baf03f41f0b0a9410d8e6038e3124a"
SRCREV_machine_qemuarm64 ?= "49e2f2d48ac2bb3f0fc379ee87651a2eaeab2cc7"
SRCREV_machine_qemumips ?= "f821004dd2dae30004c54b2d2ec2563aee11be8a"
SRCREV_machine_qemuppc ?= "49e2f2d48ac2bb3f0fc379ee87651a2eaeab2cc7"
SRCREV_machine_qemux86 ?= "49e2f2d48ac2bb3f0fc379ee87651a2eaeab2cc7"
SRCREV_machine_qemux86-64 ?= "49e2f2d48ac2bb3f0fc379ee87651a2eaeab2cc7"
SRCREV_machine_qemumips64 ?= "0cf340000b9c2b91d1086d4c34f88eecfc257562"
SRCREV_machine ?= "49e2f2d48ac2bb3f0fc379ee87651a2eaeab2cc7"
SRCREV_meta ?= "1fb60461e4522bba62b4b34e9520128c959d2437"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.13"

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
