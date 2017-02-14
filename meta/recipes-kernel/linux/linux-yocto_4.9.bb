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

SRCREV_machine_qemuarm ?= "b486cccff0f7d0b8329d1563cb7f6f37bd61218c"
SRCREV_machine_qemuarm64 ?= "d0c402280c0fb424f02b7d152c8a008d9ac267df"
SRCREV_machine_qemumips ?= "25272505ce62183927098e2834dc039bef127043"
SRCREV_machine_qemuppc ?= "d0c402280c0fb424f02b7d152c8a008d9ac267df"
SRCREV_machine_qemux86 ?= "d0c402280c0fb424f02b7d152c8a008d9ac267df"
SRCREV_machine_qemux86-64 ?= "d0c402280c0fb424f02b7d152c8a008d9ac267df"
SRCREV_machine_qemumips64 ?= "2ee2ed6d782e36fd705cc8f6ad9de98606ca227e"
SRCREV_machine ?= "d0c402280c0fb424f02b7d152c8a008d9ac267df"
SRCREV_meta ?= "b6e2f3d63bd3e8fc2663c7e10356db916e496207"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.9"

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
