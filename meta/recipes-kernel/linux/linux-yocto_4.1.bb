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

SRCREV_machine_qemuarm ?= "694cc606807a8e5cce762027f51b4179520c9238"
SRCREV_machine_qemuarm64 ?= "304caa9480f19875e717faf3cad8cb7ecd758733"
SRCREV_machine_qemumips ?= "9150f6c42a7f3d61c150dd0cc2f2de8749dceac9"
SRCREV_machine_qemuppc ?= "304caa9480f19875e717faf3cad8cb7ecd758733"
SRCREV_machine_qemux86 ?= "304caa9480f19875e717faf3cad8cb7ecd758733"
SRCREV_machine_qemux86-64 ?= "304caa9480f19875e717faf3cad8cb7ecd758733"
SRCREV_machine_qemumips64 ?= "e068452acbc753215c7e26d4e81cdf437b53b988"
SRCREV_machine ?= "304caa9480f19875e717faf3cad8cb7ecd758733"
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
