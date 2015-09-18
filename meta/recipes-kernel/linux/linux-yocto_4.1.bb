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

SRCREV_machine_qemuarm ?= "f830ab33799d6e0b5433c18c5c6c3c68dce17815"
SRCREV_machine_qemuarm64 ?= "f830ab33799d6e0b5433c18c5c6c3c68dce17815"
SRCREV_machine_qemumips ?= "f830ab33799d6e0b5433c18c5c6c3c68dce17815"
SRCREV_machine_qemuppc ?= "f830ab33799d6e0b5433c18c5c6c3c68dce17815"
SRCREV_machine_qemux86 ?= "f830ab33799d6e0b5433c18c5c6c3c68dce17815"
SRCREV_machine_qemux86-64 ?= "f830ab33799d6e0b5433c18c5c6c3c68dce17815"
SRCREV_machine_qemumips64 ?= "f830ab33799d6e0b5433c18c5c6c3c68dce17815"
SRCREV_machine ?= "f830ab33799d6e0b5433c18c5c6c3c68dce17815"
SRCREV_meta ?= "ef417dc3dbac098e2273b9157bf7ea8b1ade0ec5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.6"

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
