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

SRCREV_machine_qemuarm ?= "371697c9008e2f26edba9b4a3baaf327a530fb7f"
SRCREV_machine_qemuarm64 ?= "b259a5d744e8955a03405c6b59c5a61724755ea8"
SRCREV_machine_qemumips ?= "ea0bdf9b515d2ceafaf333564594ca1bfbc0e50a"
SRCREV_machine_qemuppc ?= "b259a5d744e8955a03405c6b59c5a61724755ea8"
SRCREV_machine_qemux86 ?= "b259a5d744e8955a03405c6b59c5a61724755ea8"
SRCREV_machine_qemux86-64 ?= "b259a5d744e8955a03405c6b59c5a61724755ea8"
SRCREV_machine_qemumips64 ?= "d5434bd8cfdbefbe4dcde9743e61383ce8a944c9"
SRCREV_machine ?= "b259a5d744e8955a03405c6b59c5a61724755ea8"
SRCREV_meta ?= "805ea440c791acee4617820ab32e93f1ccbd9ae2"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.10.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.10;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.10"

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
