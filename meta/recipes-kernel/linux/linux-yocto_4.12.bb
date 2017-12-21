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

SRCREV_machine_qemuarm ?= "d293ce5ce9097c2a269cabed7ae6fcc66f92af09"
SRCREV_machine_qemuarm64 ?= "79394be8305b3c3ba2d6711ae48e08bf015fbae9"
SRCREV_machine_qemumips ?= "3bac03d93aa8eda49cae6563ba13996460dffd4f"
SRCREV_machine_qemuppc ?= "79394be8305b3c3ba2d6711ae48e08bf015fbae9"
SRCREV_machine_qemux86 ?= "79394be8305b3c3ba2d6711ae48e08bf015fbae9"
SRCREV_machine_qemux86-64 ?= "79394be8305b3c3ba2d6711ae48e08bf015fbae9"
SRCREV_machine_qemumips64 ?= "8c0a56e5a553ac3a985949ae8d0fc35d54779756"
SRCREV_machine ?= "79394be8305b3c3ba2d6711ae48e08bf015fbae9"
SRCREV_meta ?= "b66a4f9730339b3c0c4af1db03dd26da71e419d5"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.12.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.12;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.12.18"

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
