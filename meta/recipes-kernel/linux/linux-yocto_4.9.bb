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

SRCREV_machine_qemuarm ?= "d5283083c827e18c5500d80c25b0a5eb76f64607"
SRCREV_machine_qemuarm64 ?= "cd8f9254aaaacbfa7f45fcc0bf2bb307615a174b"
SRCREV_machine_qemumips ?= "46f372c4c36868dd3546984efa408c2fe41c8fba"
SRCREV_machine_qemuppc ?= "cd8f9254aaaacbfa7f45fcc0bf2bb307615a174b"
SRCREV_machine_qemux86 ?= "cd8f9254aaaacbfa7f45fcc0bf2bb307615a174b"
SRCREV_machine_qemux86-64 ?= "cd8f9254aaaacbfa7f45fcc0bf2bb307615a174b"
SRCREV_machine_qemumips64 ?= "b88c2da88e31179a9cf7c22651e69adefcaa250f"
SRCREV_machine ?= "cd8f9254aaaacbfa7f45fcc0bf2bb307615a174b"
SRCREV_meta ?= "cdbd35c54b6a62e4fd543164f1dcdf92c85cff2d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.57"

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
