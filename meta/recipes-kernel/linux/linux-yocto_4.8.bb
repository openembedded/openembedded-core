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

SRCREV_machine_qemuarm ?= "a004864814e34348c6dc5f745af3f775c55c0510"
SRCREV_machine_qemuarm64 ?= "eb61693c773924d742aeea5e03b88d7554db61c3"
SRCREV_machine_qemumips ?= "89f99732db8a8082898a396e67a34472a1017032"
SRCREV_machine_qemuppc ?= "e03132839bac2cef030ff70b3e19c0fb4816a9fd"
SRCREV_machine_qemux86 ?= "9acf42c4475aa0699b2692d7f27f22d699b36337"
SRCREV_machine_qemux86-64 ?= "9acf42c4475aa0699b2692d7f27f22d699b36337"
SRCREV_machine_qemumips64 ?= "ff92ef8e2b67415b5e6fd4cd15489cb43de8c932"
SRCREV_machine ?= "9acf42c4475aa0699b2692d7f27f22d699b36337"
SRCREV_meta ?= "1dc615a67779fdfd36548fd48e54bd19b6e6209e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.10"

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
