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

SRCREV_machine_qemuarm ?= "0352ebb49ba09bd09dee69d6df4af1388d3f4992"
SRCREV_machine_qemuarm64 ?= "74159a303c210bca8ea5c5636fc5b17d47e51aa1"
SRCREV_machine_qemumips ?= "46ec81f8b6d8f6c6906280651d2aa5e18d3bb63f"
SRCREV_machine_qemuppc ?= "74159a303c210bca8ea5c5636fc5b17d47e51aa1"
SRCREV_machine_qemux86 ?= "74159a303c210bca8ea5c5636fc5b17d47e51aa1"
SRCREV_machine_qemux86-64 ?= "74159a303c210bca8ea5c5636fc5b17d47e51aa1"
SRCREV_machine_qemumips64 ?= "9c23f12286ee4aafe695153f4ebf5b9d4cdfd909"
SRCREV_machine ?= "74159a303c210bca8ea5c5636fc5b17d47e51aa1"
SRCREV_meta ?= "833214b33303ecde02fdcd1a9bc7901ded6a7976"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.2"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
