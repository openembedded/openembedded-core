inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/default/base"
KBRANCH_qemux86  = "standard/default/common-pc/base"
KBRANCH_qemux86-64  = "standard/default/common-pc-64/base"
KBRANCH_qemuppc  = "standard/default/qemu-ppc32"
KBRANCH_qemumips = "standard/default/mti-malta32-be"
KBRANCH_qemuarm  = "standard/default/arm-versatile-926ejs"

LINUX_VERSION ?= "3.2.18"

SRCREV_machine_qemuarm ?= "259cff0813417d16baaaaf44b00a9f75103ebfcb"
SRCREV_machine_qemumips ?= "9a803ecc05b3af481036a6d9bb0da3a899be3074"
SRCREV_machine_qemuppc ?= "466746d1fe6370957ba087f9ca6f2e31201b2162"
SRCREV_machine_qemux86 ?= "c228cadee60f0ada73d11a36f6932f50a1c52d48"
SRCREV_machine_qemux86-64 ?= "b95a0ae3773545fa0ed9a47088d0361527c42e6c"
SRCREV_machine ?= "8b8cfaaab2b8d79ac56e8c9a85bad9ae7bca399c"
SRCREV_meta ?= "0a18db9fc89a0e030e8c7b8d01fe03c5ca4197e3"

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
