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

SRCREV_machine_qemuarm ?= "97253eca8592c9cba7c7665277e1118b048b9639"
SRCREV_machine_qemuarm64 ?= "f4ba3db6e599ed41d1c676f9086ad8b97fd55046"
SRCREV_machine_qemumips ?= "52e935b59800868731e7620caf49cc257f1b9946"
SRCREV_machine_qemuppc ?= "f4ba3db6e599ed41d1c676f9086ad8b97fd55046"
SRCREV_machine_qemux86 ?= "f4ba3db6e599ed41d1c676f9086ad8b97fd55046"
SRCREV_machine_qemux86-64 ?= "f4ba3db6e599ed41d1c676f9086ad8b97fd55046"
SRCREV_machine_qemumips64 ?= "7b6d7feb4b0143d6f9146784f6072ffd171dd7ba"
SRCREV_machine ?= "f4ba3db6e599ed41d1c676f9086ad8b97fd55046"
SRCREV_meta ?= "ba11a3e8f1bc465c9de3cf00e8e60437db60e886"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.10.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.10;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.10.17"

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
