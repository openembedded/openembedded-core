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

SRCREV_machine_qemuarm ?= "cdfdcbd7ee553dc2e483813054a378ef9eaa44b6"
SRCREV_machine_qemuarm64 ?= "bc64c8124504681545cb97a22b69a4e4bfeb55e2"
SRCREV_machine_qemumips ?= "b1f51ad1d4e95be437bf7725e094d6ad1b8286b0"
SRCREV_machine_qemuppc ?= "bc64c8124504681545cb97a22b69a4e4bfeb55e2"
SRCREV_machine_qemux86 ?= "bc64c8124504681545cb97a22b69a4e4bfeb55e2"
SRCREV_machine_qemux86-64 ?= "bc64c8124504681545cb97a22b69a4e4bfeb55e2"
SRCREV_machine_qemumips64 ?= "4b17815085dbd2a0408d7969bf4689a4c2b12942"
SRCREV_machine ?= "bc64c8124504681545cb97a22b69a4e4bfeb55e2"
SRCREV_meta ?= "870134f4bfa6208d6e5339e065486be3b6e693a5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.13"

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
