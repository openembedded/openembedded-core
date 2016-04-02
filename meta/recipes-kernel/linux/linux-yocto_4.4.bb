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

SRCREV_machine_qemuarm ?= "03f244ec7482dbc1c924af73a317dd3d12f00132"
SRCREV_machine_qemuarm64 ?= "08943f2bbd507f8b7f5ef101ccc4755cbec79aad"
SRCREV_machine_qemumips ?= "4cd732b14cf4bdb51b7b565783bbc2ce5ab9a943"
SRCREV_machine_qemuppc ?= "08943f2bbd507f8b7f5ef101ccc4755cbec79aad"
SRCREV_machine_qemux86 ?= "08943f2bbd507f8b7f5ef101ccc4755cbec79aad"
SRCREV_machine_qemux86-64 ?= "08943f2bbd507f8b7f5ef101ccc4755cbec79aad"
SRCREV_machine_qemumips64 ?= "699473bc2476afa85e47833f0f47aaf44a799257"
SRCREV_machine ?= "08943f2bbd507f8b7f5ef101ccc4755cbec79aad"
SRCREV_meta ?= "770996a263e22562c81f48fde0f0dc647156abce"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.3"

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

SRC_URI_append = " file://0001-Fix-qemux86-pat-issue.patch"
