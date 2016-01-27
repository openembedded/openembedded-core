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

SRCREV_machine_qemuarm ?= "b635f136b1c5ffd0f570b1799c9265ba832efc3a"
SRCREV_machine_qemuarm64 ?= "9acf3d237d07758701d56c75cc2058f05a974899"
SRCREV_machine_qemumips ?= "fd7be86f6cafbf3947fdffdee23b0e9ca9e4abb0"
SRCREV_machine_qemuppc ?= "9acf3d237d07758701d56c75cc2058f05a974899"
SRCREV_machine_qemux86 ?= "9acf3d237d07758701d56c75cc2058f05a974899"
SRCREV_machine_qemux86-64 ?= "9acf3d237d07758701d56c75cc2058f05a974899"
SRCREV_machine_qemumips64 ?= "45cc8f9f06eb83d7804d03690e7fa0d76b060e16"
SRCREV_machine ?= "9acf3d237d07758701d56c75cc2058f05a974899"
SRCREV_meta ?= "fde1381efc83859fcd873521bd1243646b22ef46"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;name=machine;branch=${KBRANCH}; \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

LINUX_VERSION ?= "4.1.16"

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
