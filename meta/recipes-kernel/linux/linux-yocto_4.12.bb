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

SRCREV_machine_qemuarm ?= "6155fbae7a6b42a7608b9f251fa46cef28c0fe67"
SRCREV_machine_qemuarm64 ?= "392959d6dad698bfb62de561164c2d19e17841dd"
SRCREV_machine_qemumips ?= "d46a4aebe29dcced0fea9718c249c536ab06977f"
SRCREV_machine_qemuppc ?= "392959d6dad698bfb62de561164c2d19e17841dd"
SRCREV_machine_qemux86 ?= "392959d6dad698bfb62de561164c2d19e17841dd"
SRCREV_machine_qemux86-64 ?= "392959d6dad698bfb62de561164c2d19e17841dd"
SRCREV_machine_qemumips64 ?= "10216660f9d902f76f36514ea4ef04530e87702d"
SRCREV_machine ?= "392959d6dad698bfb62de561164c2d19e17841dd"
SRCREV_meta ?= "ca1bda8dfd3325db04dda19a531edeb66ac0d84b"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.20"

PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"

COMPATIBLE_MACHINE = "qemuarm|qemuarm64|qemux86|qemuppc|qemumips|qemumips64|qemux86-64"

# Functionality flags
KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc"
KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"
KERNEL_FEATURES_append_qemuall=" cfg/virtio.scc"
KERNEL_FEATURES_append_qemux86=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound.scc cfg/paravirt_kvm.scc"
KERNEL_FEATURES_append = " ${@bb.utils.contains("TUNE_FEATURES", "mx32", " cfg/x32.scc", "" ,d)}"
