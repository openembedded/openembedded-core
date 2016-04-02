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

SRCREV_machine_qemuarm ?= "75d5fa3b94efe0eb2101946d51015ce5bdc126cf"
SRCREV_machine_qemuarm64 ?= "f5b08e95bc03f9819fa9d7ff57c698fbe2c16cc5"
SRCREV_machine_qemumips ?= "5b7969dfff341c7d81b3726e0bb100cafa1cbfa1"
SRCREV_machine_qemuppc ?= "f5b08e95bc03f9819fa9d7ff57c698fbe2c16cc5"
SRCREV_machine_qemux86 ?= "f5b08e95bc03f9819fa9d7ff57c698fbe2c16cc5"
SRCREV_machine_qemux86-64 ?= "f5b08e95bc03f9819fa9d7ff57c698fbe2c16cc5"
SRCREV_machine_qemumips64 ?= "a3a35132a724752464ee13b10432cbdb2a110404"
SRCREV_machine ?= "f5b08e95bc03f9819fa9d7ff57c698fbe2c16cc5"
SRCREV_meta ?= "b9023d4c8fbbb854c26f158a079a5f54dd61964d"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.18"

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
