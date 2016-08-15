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

SRCREV_machine_qemuarm ?= "09b5eed6e905503646514803787d82f504013144"
SRCREV_machine_qemuarm64 ?= "053af7be9159a0b5a016a3e399de1484e51f26cd"
SRCREV_machine_qemumips ?= "582073e7a49b0847052be78254805f33635dc837"
SRCREV_machine_qemuppc ?= "0914d10f46f22582e955b06590768ec51e8a91c3"
SRCREV_machine_qemux86 ?= "053af7be9159a0b5a016a3e399de1484e51f26cd"
SRCREV_machine_qemux86-64 ?= "053af7be9159a0b5a016a3e399de1484e51f26cd"
SRCREV_machine_qemumips64 ?= "ca25d7656f7eaeb8454a182cfa38fb704ec85276"
SRCREV_machine ?= "053af7be9159a0b5a016a3e399de1484e51f26cd"
SRCREV_meta ?= "71dddd41d8395014d942ac8f20f1255751cb144f"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.29"

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
