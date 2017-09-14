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

SRCREV_machine_qemuarm ?= "799a97a6f555d98479a55d92e580d91e6f621118"
SRCREV_machine_qemuarm64 ?= "7078ebf077771189f5da4b676f70dddc92abba42"
SRCREV_machine_qemumips ?= "18ad0e32668a585380b7f48b5dacd288b0c882d3"
SRCREV_machine_qemuppc ?= "7078ebf077771189f5da4b676f70dddc92abba42"
SRCREV_machine_qemux86 ?= "7078ebf077771189f5da4b676f70dddc92abba42"
SRCREV_machine_qemux86-64 ?= "7078ebf077771189f5da4b676f70dddc92abba42"
SRCREV_machine_qemumips64 ?= "1b88be0ada672b2b74221d9a810d3072cb92cf37"
SRCREV_machine ?= "7078ebf077771189f5da4b676f70dddc92abba42"
SRCREV_meta ?= "804d2b3164ec25ed519fd695de9aa0908460c92e"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.87"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
