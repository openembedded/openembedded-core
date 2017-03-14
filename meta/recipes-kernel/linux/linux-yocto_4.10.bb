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

SRCREV_machine_qemuarm ?= "ad30e5e8b96e0db4fb618b1320f1a9cb5ad175a8"
SRCREV_machine_qemuarm64 ?= "827a1164b155110c65f028a13a6c5699be93bbc3"
SRCREV_machine_qemumips ?= "b21d6ab39ba16f05780aaff518a2d7274fc0ce5f"
SRCREV_machine_qemuppc ?= "827a1164b155110c65f028a13a6c5699be93bbc3"
SRCREV_machine_qemux86 ?= "827a1164b155110c65f028a13a6c5699be93bbc3"
SRCREV_machine_qemux86-64 ?= "827a1164b155110c65f028a13a6c5699be93bbc3"
SRCREV_machine_qemumips64 ?= "4cd89b5ee382b61f90b81903934a1edfd836b617"
SRCREV_machine ?= "827a1164b155110c65f028a13a6c5699be93bbc3"
SRCREV_meta ?= "69bfa872343a413322c2a1289f25d24a2db2728d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.10.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.10;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.10.2"

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
