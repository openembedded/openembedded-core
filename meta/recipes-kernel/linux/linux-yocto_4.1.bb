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

SRCREV_machine_qemuarm ?= "f20a35023c5d96818a7ac061fe8f08432d8a5088"
SRCREV_machine_qemuarm64 ?= "c71e0bd7f702aa090b9733ad4e0382ac6c5908dd"
SRCREV_machine_qemumips ?= "d1367fe15275de31711b21fa736a86f99fb300c5"
SRCREV_machine_qemuppc ?= "e97556943b41cb6422842a348ea50a3630afe78f"
SRCREV_machine_qemux86 ?= "c71e0bd7f702aa090b9733ad4e0382ac6c5908dd"
SRCREV_machine_qemux86-64 ?= "c71e0bd7f702aa090b9733ad4e0382ac6c5908dd"
SRCREV_machine_qemumips64 ?= "b23f3f4a74da40c3302d95f601af333c546e6999"
SRCREV_machine ?= "c71e0bd7f702aa090b9733ad4e0382ac6c5908dd"
SRCREV_meta ?= "4e12cb8f8e06636f2058ea0ab3096ed38228a88b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.49"

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
