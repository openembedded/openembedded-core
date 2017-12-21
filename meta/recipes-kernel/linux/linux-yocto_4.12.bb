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

SRCREV_machine_qemuarm ?= "355d43fde266ab8062616a8226f63f0fe9ba8693"
SRCREV_machine_qemuarm64 ?= "1b85adfc0b817bc39efc48dcb3405808daf2b505"
SRCREV_machine_qemumips ?= "db46814f088b53d656be50c1ff8a6ac0e120a312"
SRCREV_machine_qemuppc ?= "1b85adfc0b817bc39efc48dcb3405808daf2b505"
SRCREV_machine_qemux86 ?= "1b85adfc0b817bc39efc48dcb3405808daf2b505"
SRCREV_machine_qemux86-64 ?= "1b85adfc0b817bc39efc48dcb3405808daf2b505"
SRCREV_machine_qemumips64 ?= "74b3f662fcc5672389b05ae572858e34a123e570"
SRCREV_machine ?= "1b85adfc0b817bc39efc48dcb3405808daf2b505"
SRCREV_meta ?= "3574bb061c1bfbdcf4df8308870c03f88ef0788f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.16"

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
