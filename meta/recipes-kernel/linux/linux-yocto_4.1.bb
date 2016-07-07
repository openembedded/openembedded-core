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

SRCREV_machine_qemuarm ?= "ce593534958d0bc6a3d1bc3036f425b9d7d9f5f6"
SRCREV_machine_qemuarm64 ?= "6ea8af4a9bc8797d71e14d9e641d9e28b1dee917"
SRCREV_machine_qemumips ?= "f1566b7d9fafd16b5dd98a313ffae1cab493a1c3"
SRCREV_machine_qemuppc ?= "6ea8af4a9bc8797d71e14d9e641d9e28b1dee917"
SRCREV_machine_qemux86 ?= "6ea8af4a9bc8797d71e14d9e641d9e28b1dee917"
SRCREV_machine_qemux86-64 ?= "6ea8af4a9bc8797d71e14d9e641d9e28b1dee917"
SRCREV_machine_qemumips64 ?= "8bac527ccf5cef97eac0dec7d94fb8cb668d7e13"
SRCREV_machine ?= "6ea8af4a9bc8797d71e14d9e641d9e28b1dee917"
SRCREV_meta ?= "886dacbd6290bbd5bb0632883f661fb57b232d94"

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
