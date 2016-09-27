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

SRCREV_machine_qemuarm ?= "3a2b4be23e0d3115962cd49302f80679c86a2df4"
SRCREV_machine_qemuarm64 ?= "8edbf10bb6e0759bfb6f18035d1b4773f573d33c"
SRCREV_machine_qemumips ?= "fc863e7a0f5664eef65ff36f35cc41c0be59ef3b"
SRCREV_machine_qemuppc ?= "8edbf10bb6e0759bfb6f18035d1b4773f573d33c"
SRCREV_machine_qemux86 ?= "8edbf10bb6e0759bfb6f18035d1b4773f573d33c"
SRCREV_machine_qemux86-64 ?= "8edbf10bb6e0759bfb6f18035d1b4773f573d33c"
SRCREV_machine_qemumips64 ?= "dd0e6bc1cb1973d72ff9d3b4ee44a0edc04fea3c"
SRCREV_machine ?= "8edbf10bb6e0759bfb6f18035d1b4773f573d33c"
SRCREV_meta ?= "d0937e67c3c11fd0e6429bd050a86f6c295560e1"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.8-rc8"

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
