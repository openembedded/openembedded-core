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

SRCREV_machine_qemuarm ?= "bdda97014e55200e704ecd8d112da09176dcb7dc"
SRCREV_machine_qemuarm64 ?= "fe7ff38448d530db27ac31cbb086cb6fdf94bb7f"
SRCREV_machine_qemumips ?= "f60887555f78ae5f5338d41181e848082109429b"
SRCREV_machine_qemuppc ?= "fe7ff38448d530db27ac31cbb086cb6fdf94bb7f"
SRCREV_machine_qemux86 ?= "fe7ff38448d530db27ac31cbb086cb6fdf94bb7f"
SRCREV_machine_qemux86-64 ?= "fe7ff38448d530db27ac31cbb086cb6fdf94bb7f"
SRCREV_machine_qemumips64 ?= "b783512062c5081fa9b01e89e811d0508e62895f"
SRCREV_machine ?= "fe7ff38448d530db27ac31cbb086cb6fdf94bb7f"
SRCREV_meta ?= "b7ce076447c0e3cc07f8d3ed4a11070fdeae41e6"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.10"

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
