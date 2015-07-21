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

SRCREV_machine_qemuarm ?= "c4df99c91fc947afb7010a11eceeef0e05bf964f"
SRCREV_machine_qemuarm64 ?= "4e30e64c44df9e59bd13239951bb8d2b5b276e6f"
SRCREV_machine_qemumips ?= "6a24f7e60b9b2ecc1579f5f767be5845086533e1"
SRCREV_machine_qemuppc ?= "4e30e64c44df9e59bd13239951bb8d2b5b276e6f"
SRCREV_machine_qemux86 ?= "4e30e64c44df9e59bd13239951bb8d2b5b276e6f"
SRCREV_machine_qemux86-64 ?= "4e30e64c44df9e59bd13239951bb8d2b5b276e6f"
SRCREV_machine_qemumips64 ?= "5c2b3697082a4ec6641aa5a8eca3974ca609cecf"
SRCREV_machine ?= "4e30e64c44df9e59bd13239951bb8d2b5b276e6f"
SRCREV_meta ?= "45393dd54f5ad77d43014c407c2b3520da42f427"

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
