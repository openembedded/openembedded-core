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

SRCREV_machine_qemuarm ?= "bb61314e5c67a13c7da2217e434dfb20053a0203"
SRCREV_machine_qemuarm64 ?= "eb7faf5b9bfe94ac2cb8e6a28e18fb39a8b720a7"
SRCREV_machine_qemumips ?= "4bcebcabb514ba8f9b3f8f487f3595a5c2b5860f"
SRCREV_machine_qemuppc ?= "3913c9c8dad1a3fef93d3d5721146ebe15dcc4d3"
SRCREV_machine_qemux86 ?= "edc90f45a716ffe8e16cebaaf3b5db070af0280a"
SRCREV_machine_qemux86-64 ?= "edc90f45a716ffe8e16cebaaf3b5db070af0280a"
SRCREV_machine_qemumips64 ?= "6d7fbdbb7e466be645c34eed7bb2b532a9431a85"
SRCREV_machine ?= "edc90f45a716ffe8e16cebaaf3b5db070af0280a"
SRCREV_meta ?= "54b3e9b768651ca15bf65ae1c72dfe6d957285f0"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.14;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.14.24"

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
