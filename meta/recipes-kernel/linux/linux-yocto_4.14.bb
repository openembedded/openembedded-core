KBRANCH ?= "v4.14/standard/base"

require recipes-kernel/linux/linux-yocto.inc

# board specific branches
KBRANCH_qemuarm  ?= "v4.14/standard/arm-versatile-926ejs"
KBRANCH_qemuarm64 ?= "v4.14/standard/qemuarm64"
KBRANCH_qemumips ?= "v4.14/standard/mti-malta32"
KBRANCH_qemuppc  ?= "v4.14/standard/qemuppc"
KBRANCH_qemux86  ?= "v4.14/standard/base"
KBRANCH_qemux86-64 ?= "v4.14/standard/base"
KBRANCH_qemumips64 ?= "v4.14/standard/mti-malta64"

SRCREV_machine_qemuarm ?= "773cb1d2d59644bf9574abed9803f8ec12bbabe1"
SRCREV_machine_qemuarm64 ?= "537602db26fe2d26c1959f1ecb43966770c10ff2"
SRCREV_machine_qemumips ?= "5fad9fa9377a7badd71ae4c1f1b1108d8cfcb047"
SRCREV_machine_qemuppc ?= "537602db26fe2d26c1959f1ecb43966770c10ff2"
SRCREV_machine_qemux86 ?= "537602db26fe2d26c1959f1ecb43966770c10ff2"
SRCREV_machine_qemux86-64 ?= "537602db26fe2d26c1959f1ecb43966770c10ff2"
SRCREV_machine_qemumips64 ?= "141cd6a1644eade83d4d43675a9ab11824ea01ea"
SRCREV_machine ?= "537602db26fe2d26c1959f1ecb43966770c10ff2"
SRCREV_meta ?= "b4468b54b2a7e7fbdf207ead7ca925bc24976d1b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.19"

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
