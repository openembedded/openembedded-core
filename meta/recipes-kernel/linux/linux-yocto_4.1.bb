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

SRCREV_machine_qemuarm ?= "2494184ad34764bdfa1bfd9a57eabfb34d170c0e"
SRCREV_machine_qemuarm64 ?= "2c30f87db7824e90b0b096eee3a5b7f93c84b079"
SRCREV_machine_qemumips ?= "85a76c24f5aaa99618fdb15d5f3966a2e03cd572"
SRCREV_machine_qemuppc ?= "2c30f87db7824e90b0b096eee3a5b7f93c84b079"
SRCREV_machine_qemux86 ?= "2c30f87db7824e90b0b096eee3a5b7f93c84b079"
SRCREV_machine_qemux86-64 ?= "2c30f87db7824e90b0b096eee3a5b7f93c84b079"
SRCREV_machine_qemumips64 ?= "79181dc5284d9831d240d7ba58a2b574f6528b90"
SRCREV_machine ?= "2c30f87db7824e90b0b096eee3a5b7f93c84b079"
SRCREV_meta ?= "30c4b4bd79c5d774de6dcf23d0deab554e31f3d4"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.13"

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
