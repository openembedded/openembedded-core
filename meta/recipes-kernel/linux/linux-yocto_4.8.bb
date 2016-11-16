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

SRCREV_machine_qemuarm ?= "ae6282e4c25529f2649f8fa31d45c1b5bf5daf42"
SRCREV_machine_qemuarm64 ?= "efbdfa1ed95f7f0b4243351ffdb2d30e58308aeb"
SRCREV_machine_qemumips ?= "87fd523e80256e404710624b5c45d76da369f9b5"
SRCREV_machine_qemuppc ?= "efbdfa1ed95f7f0b4243351ffdb2d30e58308aeb"
SRCREV_machine_qemux86 ?= "efbdfa1ed95f7f0b4243351ffdb2d30e58308aeb"
SRCREV_machine_qemux86-64 ?= "efbdfa1ed95f7f0b4243351ffdb2d30e58308aeb"
SRCREV_machine_qemumips64 ?= "13669dd21e6c8e0cfccc1c6ccc3f83e272f0f6d1"
SRCREV_machine ?= "efbdfa1ed95f7f0b4243351ffdb2d30e58308aeb"
SRCREV_meta ?= "d7a58814eee97e7bf410e418ad087a1f7bb05ab9"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8.6"

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
