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

SRCREV_machine_qemuarm ?= "a1201019dd589ee33964da46a8402c7e00133ee2"
SRCREV_machine_qemuarm64 ?= "0148b3601f29b159b4f84c1478ff1859bbd48efe"
SRCREV_machine_qemumips ?= "2f8a14c3f7c6372b00bfa4fea186f277d8125963"
SRCREV_machine_qemuppc ?= "0148b3601f29b159b4f84c1478ff1859bbd48efe"
SRCREV_machine_qemux86 ?= "0148b3601f29b159b4f84c1478ff1859bbd48efe"
SRCREV_machine_qemux86-64 ?= "0148b3601f29b159b4f84c1478ff1859bbd48efe"
SRCREV_machine_qemumips64 ?= "b65cb9e6c3ed6e8f97150d4de0f651c7cd1c4fdf"
SRCREV_machine ?= "0148b3601f29b159b4f84c1478ff1859bbd48efe"
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
