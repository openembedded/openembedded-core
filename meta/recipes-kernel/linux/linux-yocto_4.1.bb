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

SRCREV_machine_qemuarm ?= "5049ec1a05cf7c9ff19c892b42d67f17974d3a96"
SRCREV_machine_qemuarm64 ?= "c9b497be080fcd42adab967fc972f225c9f5b84e"
SRCREV_machine_qemumips ?= "c01dfca5d22960a5b9e4aa47d9c062ecbded5857"
SRCREV_machine_qemuppc ?= "c9b497be080fcd42adab967fc972f225c9f5b84e"
SRCREV_machine_qemux86 ?= "c9b497be080fcd42adab967fc972f225c9f5b84e"
SRCREV_machine_qemux86-64 ?= "c9b497be080fcd42adab967fc972f225c9f5b84e"
SRCREV_machine_qemumips64 ?= "b8342b84b3a914651c86f3ae463e4f8c6de36da0"
SRCREV_machine ?= "c9b497be080fcd42adab967fc972f225c9f5b84e"
SRCREV_meta ?= "9f68667031354532563766a3d04ca8a618e9177a"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.26"

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
