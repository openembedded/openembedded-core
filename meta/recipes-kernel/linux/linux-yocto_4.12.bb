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

SRCREV_machine_qemuarm ?= "7122a8d189df29393419b3352ea11482efaa417a"
SRCREV_machine_qemuarm64 ?= "22f73c1fe99198a7b37aa5c76978fe19d301fd88"
SRCREV_machine_qemumips ?= "1cff37b7a2bbee4bb99118e57d228ac301fb880b"
SRCREV_machine_qemuppc ?= "22f73c1fe99198a7b37aa5c76978fe19d301fd88"
SRCREV_machine_qemux86 ?= "22f73c1fe99198a7b37aa5c76978fe19d301fd88"
SRCREV_machine_qemux86-64 ?= "22f73c1fe99198a7b37aa5c76978fe19d301fd88"
SRCREV_machine_qemumips64 ?= "3879cb9ed592f8d5db6b35c51761818ad60494da"
SRCREV_machine ?= "22f73c1fe99198a7b37aa5c76978fe19d301fd88"
SRCREV_meta ?= "19d815d5a34bfaad95d87cc097cef18b594daac8"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.20"

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
