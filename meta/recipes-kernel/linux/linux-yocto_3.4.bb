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

SRCREV_machine_qemuarm ?= "bc7dd2ebe7f5443fb9d89d46106053cfc69b5d7f"
SRCREV_machine_qemumips ?= "5a2ad0078de3d24a6e8e3f3c9a7426a08a86717d"
SRCREV_machine_qemuppc ?= "f92ab2a332627c0213ecfdfc873d470be008f86b"
SRCREV_machine_qemux86 ?= "780ab7e11f03931295e8f0e29707f2abb688e9ad"
SRCREV_machine_qemux86-64 ?= "780ab7e11f03931295e8f0e29707f2abb688e9ad"
SRCREV_machine ?= "780ab7e11f03931295e8f0e29707f2abb688e9ad"
SRCREV_meta ?= "3fd089debe624c642d7b4d9363f853021d1675b2"

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

require recipes-kernel/linux/linux-tools.inc
