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

SRCREV_machine_qemuarm ?= "ebd7956fb8f43632fff9537579d2788532ec5dad"
SRCREV_machine_qemuarm64 ?= "087b65ddac049813eef2513c4440cb1d02971357"
SRCREV_machine_qemumips ?= "2e81d7a5cc2cbfae377f4d07d85c34800c04e3c2"
SRCREV_machine_qemuppc ?= "087b65ddac049813eef2513c4440cb1d02971357"
SRCREV_machine_qemux86 ?= "087b65ddac049813eef2513c4440cb1d02971357"
SRCREV_machine_qemux86-64 ?= "087b65ddac049813eef2513c4440cb1d02971357"
SRCREV_machine_qemumips64 ?= "af223adb1634de8cb279ff809c6d13266b87e5dc"
SRCREV_machine ?= "087b65ddac049813eef2513c4440cb1d02971357"
SRCREV_meta ?= "8c71361007816dcabe297f40cd5a38ed5d7d7599"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.4"

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
