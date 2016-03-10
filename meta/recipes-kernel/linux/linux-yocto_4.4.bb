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

SRCREV_machine_qemuarm ?= "9ae4dd8747fc85511ebc4b460dd5cc5049abd9c9"
SRCREV_machine_qemuarm64 ?= "66009f8977a62d3bd29d4b89a8e29d1095524aea"
SRCREV_machine_qemumips ?= "9f0cb8fa7b1b3e167bb8d70a3433246830ada786"
SRCREV_machine_qemuppc ?= "66009f8977a62d3bd29d4b89a8e29d1095524aea"
SRCREV_machine_qemux86 ?= "66009f8977a62d3bd29d4b89a8e29d1095524aea"
SRCREV_machine_qemux86-64 ?= "66009f8977a62d3bd29d4b89a8e29d1095524aea"
SRCREV_machine_qemumips64 ?= "d941e61b3aadae55344addb8bbb778a0c72087bc"
SRCREV_machine ?= "66009f8977a62d3bd29d4b89a8e29d1095524aea"
SRCREV_meta ?= "0ae928f7664c97fcce9411a6323ee9b376a59996"

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

SRC_URI_append = " file://0001-Fix-qemux86-pat-issue.patch"
