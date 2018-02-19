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

SRCREV_machine_qemuarm ?= "9728502c1fc96a1348ff06e1be8df75bee168032"
SRCREV_machine_qemuarm64 ?= "1c4ad569af3e23a77994235435040e322908687f"
SRCREV_machine_qemumips ?= "b04e654320e56fa42e477698dbf61d99f0bb4501"
SRCREV_machine_qemuppc ?= "1c4ad569af3e23a77994235435040e322908687f"
SRCREV_machine_qemux86 ?= "1c4ad569af3e23a77994235435040e322908687f"
SRCREV_machine_qemux86-64 ?= "1c4ad569af3e23a77994235435040e322908687f"
SRCREV_machine_qemumips64 ?= "7a297c3848d02c46acd40e3d2f285c7905f8134c"
SRCREV_machine ?= "1c4ad569af3e23a77994235435040e322908687f"
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
