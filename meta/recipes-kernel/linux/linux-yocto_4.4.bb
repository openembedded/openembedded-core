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

SRCREV_machine_qemuarm ?= "d1fcd96376a6ef0e8003914ea5df043ede813f92"
SRCREV_machine_qemuarm64 ?= "8361321fec015bc3823d01dad25db7f3af31b6d5"
SRCREV_machine_qemumips ?= "55be46bf1e4c5452a01a3e1f72a6408f38a57fc0"
SRCREV_machine_qemuppc ?= "8361321fec015bc3823d01dad25db7f3af31b6d5"
SRCREV_machine_qemux86 ?= "8361321fec015bc3823d01dad25db7f3af31b6d5"
SRCREV_machine_qemux86-64 ?= "8361321fec015bc3823d01dad25db7f3af31b6d5"
SRCREV_machine_qemumips64 ?= "f9bf508ee3fdccebe3554b968b43675cafe0a5a1"
SRCREV_machine ?= "8361321fec015bc3823d01dad25db7f3af31b6d5"
SRCREV_meta ?= "4800a400d5ace1d4332ee18b01ac1c680e454457"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.4.14"

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
