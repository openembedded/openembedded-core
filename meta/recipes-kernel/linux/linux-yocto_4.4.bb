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

SRCREV_machine_qemuarm ?= "d22bf871c41b0b4e2836df7acebca295e98fa0b6"
SRCREV_machine_qemuarm64 ?= "c6fa2d220b5fb9b01134a06df9679436531042cf"
SRCREV_machine_qemumips ?= "7c682b7ad6d0c847593c65feb1df651fa0300b1a"
SRCREV_machine_qemuppc ?= "c6fa2d220b5fb9b01134a06df9679436531042cf"
SRCREV_machine_qemux86 ?= "c6fa2d220b5fb9b01134a06df9679436531042cf"
SRCREV_machine_qemux86-64 ?= "c6fa2d220b5fb9b01134a06df9679436531042cf"
SRCREV_machine_qemumips64 ?= "3b14a56cdca5182621ec88eabe10456256a95e80"
SRCREV_machine ?= "c6fa2d220b5fb9b01134a06df9679436531042cf"
SRCREV_meta ?= "72b622cacfda96e79477909518795dbcce6b1593"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.99"

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
