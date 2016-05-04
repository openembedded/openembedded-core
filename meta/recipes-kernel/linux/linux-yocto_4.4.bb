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

SRCREV_machine_qemuarm ?= "1394eb1f0a1988da223dfc93304222f4543d19c3"
SRCREV_machine_qemuarm64 ?= "b18090556c1d1b449233cd555c27a04d38272d6d"
SRCREV_machine_qemumips ?= "6a063a7239fd16e7a796ce4876e8677d5d02e8cc"
SRCREV_machine_qemuppc ?= "b18090556c1d1b449233cd555c27a04d38272d6d"
SRCREV_machine_qemux86 ?= "b18090556c1d1b449233cd555c27a04d38272d6d"
SRCREV_machine_qemux86-64 ?= "b18090556c1d1b449233cd555c27a04d38272d6d"
SRCREV_machine_qemumips64 ?= "3a1c6d7576908a2dd21746b1d4ab4f43b83cd824"
SRCREV_machine ?= "b18090556c1d1b449233cd555c27a04d38272d6d"
SRCREV_meta ?= "9ab4787fe2aea2ae0fcc31a5e067eaba19ef64c8"

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
