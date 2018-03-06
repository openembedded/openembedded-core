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

SRCREV_machine_qemuarm ?= "4fb13c0d7254fe9753bebdfcfe47fd1ba4487386"
SRCREV_machine_qemuarm64 ?= "cec3c008ec8f7acdb01b95fceb1ef7dff35d1877"
SRCREV_machine_qemumips ?= "9eb1f72874eeadb1f17e59faaca331971d522e12"
SRCREV_machine_qemuppc ?= "cec3c008ec8f7acdb01b95fceb1ef7dff35d1877"
SRCREV_machine_qemux86 ?= "cec3c008ec8f7acdb01b95fceb1ef7dff35d1877"
SRCREV_machine_qemux86-64 ?= "cec3c008ec8f7acdb01b95fceb1ef7dff35d1877"
SRCREV_machine_qemumips64 ?= "710cc1cb7ba1384b9b0f43fc811a18a7c5a4e03f"
SRCREV_machine ?= "cec3c008ec8f7acdb01b95fceb1ef7dff35d1877"
SRCREV_meta ?= "4f825eeb783a279216ee45ed3b9a63dd6837f7d7"

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
