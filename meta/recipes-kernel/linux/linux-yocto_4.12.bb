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

SRCREV_machine_qemuarm ?= "8dff23316c2c8a291541c94f615205a0e6cc547c"
SRCREV_machine_qemuarm64 ?= "d572780c455fcd055baf1e6ff6fd4ea2dece9df2"
SRCREV_machine_qemumips ?= "fbe79a19f0f14e46a8ac62de9ebb5691a5084e00"
SRCREV_machine_qemuppc ?= "d572780c455fcd055baf1e6ff6fd4ea2dece9df2"
SRCREV_machine_qemux86 ?= "d572780c455fcd055baf1e6ff6fd4ea2dece9df2"
SRCREV_machine_qemux86-64 ?= "d572780c455fcd055baf1e6ff6fd4ea2dece9df2"
SRCREV_machine_qemumips64 ?= "e6d567b4c476ec7edfc6721e6912daa5fa672add"
SRCREV_machine ?= "d572780c455fcd055baf1e6ff6fd4ea2dece9df2"
SRCREV_meta ?= "358b2bb4d2bc8e790f69ea1778d7d16184b1ae34"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.18"

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
