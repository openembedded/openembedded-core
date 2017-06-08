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

SRCREV_machine_qemuarm ?= "dbf351c65cf492518339ac1ee0ecb02de4beb313"
SRCREV_machine_qemuarm64 ?= "268676407913f5d496cde6cbf4052eb5acaf6237"
SRCREV_machine_qemumips ?= "80509fd80a3457f74c716e5018378efb75a35934"
SRCREV_machine_qemuppc ?= "268676407913f5d496cde6cbf4052eb5acaf6237"
SRCREV_machine_qemux86 ?= "268676407913f5d496cde6cbf4052eb5acaf6237"
SRCREV_machine_qemux86-64 ?= "268676407913f5d496cde6cbf4052eb5acaf6237"
SRCREV_machine_qemumips64 ?= "dfd0bad7365562bf39b16630c59dcb4b18a09396"
SRCREV_machine ?= "268676407913f5d496cde6cbf4052eb5acaf6237"
SRCREV_meta ?= "c3f8900923a7b56ea6231d31a1a4e81306156dc5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.71"

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
