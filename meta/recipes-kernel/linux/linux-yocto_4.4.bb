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

SRCREV_machine_qemuarm ?= "e26115bb79a4ea6a26c6052766f1e65553841d51"
SRCREV_machine_qemuarm64 ?= "26d9c4e612512486290bad4525cef5c69f5e485d"
SRCREV_machine_qemumips ?= "1b5479c3dd579143c03cfa97c25346104aff3f30"
SRCREV_machine_qemuppc ?= "26d9c4e612512486290bad4525cef5c69f5e485d"
SRCREV_machine_qemux86 ?= "26d9c4e612512486290bad4525cef5c69f5e485d"
SRCREV_machine_qemux86-64 ?= "26d9c4e612512486290bad4525cef5c69f5e485d"
SRCREV_machine_qemumips64 ?= "565650ff074c7438081bd2ff0db4d7208d666ddf"
SRCREV_machine ?= "26d9c4e612512486290bad4525cef5c69f5e485d"
SRCREV_meta ?= "4ebb8be6799bf2b738d46a3db49ebe59187f3b96"

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
