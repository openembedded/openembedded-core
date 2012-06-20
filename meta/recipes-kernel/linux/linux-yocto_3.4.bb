inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemumips64 = "standard/mti-malta64"
KBRANCH_qemumips64el = "standard/mti-malta64"
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"

SRCREV_machine_qemuarm ?= "1b83eb1b99e937b67dafa4dedba9440d596cbd9f"
SRCREV_machine_qemumips ?= "c6104a63aae262ff4238b45905ab1c57145334bc"
SRCREV_machine_qemuppc ?= "4988a7e34bccd531b511c57f93358de9fcc672a0"
SRCREV_machine_qemux86 ?= "a8291fa6f723b0182d2b7033b5d59f412ba7cf72"
SRCREV_machine_qemux86-64 ?= "a8291fa6f723b0182d2b7033b5d59f412ba7cf72"
SRCREV_machine ?= "a8291fa6f723b0182d2b7033b5d59f412ba7cf72"
SRCREV_meta ?= "aa226dcc5a1351fae49da40d77b718c4c3a76e7c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.1"

PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
