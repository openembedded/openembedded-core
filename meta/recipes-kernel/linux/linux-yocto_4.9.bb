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

SRCREV_machine_qemuarm ?= "3ff3760c2ac7cfd04904b3c9ac36567cf75594bf"
SRCREV_machine_qemuarm64 ?= "e675c4ecccb9dd9dd637f104e32b7808b4d56846"
SRCREV_machine_qemumips ?= "53bf87a63316d6355be679f0f10bdaf694a94424"
SRCREV_machine_qemuppc ?= "e675c4ecccb9dd9dd637f104e32b7808b4d56846"
SRCREV_machine_qemux86 ?= "e675c4ecccb9dd9dd637f104e32b7808b4d56846"
SRCREV_machine_qemux86-64 ?= "e675c4ecccb9dd9dd637f104e32b7808b4d56846"
SRCREV_machine_qemumips64 ?= "c7eee598e73eb4b6eb1e9d203e6e84e63a4a278a"
SRCREV_machine ?= "e675c4ecccb9dd9dd637f104e32b7808b4d56846"
SRCREV_meta ?= "0e129b67b7c5999940f2137f8fdbced06d122475"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.9.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.9;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.9.17"

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
