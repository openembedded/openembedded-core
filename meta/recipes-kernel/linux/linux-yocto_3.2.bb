inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/default/base"
KBRANCH_qemux86  = "standard/default/common-pc/base"
KBRANCH_qemux86-64  = "standard/default/common-pc-64/base"
KBRANCH_qemuppc  = "standard/default/qemu-ppc32"
KBRANCH_qemumips = "standard/default/mti-malta32-be"
KBRANCH_qemuarm  = "standard/default/arm-versatile-926ejs"

LINUX_VERSION ?= "3.2.18"

SRCREV_machine_qemuarm ?= "ebb5e65d02a352e3e8601096e1674ffc261345f2"
SRCREV_machine_qemumips ?= "62aeb307e9a731c4bba05ce4d57b9cece41a2a01"
SRCREV_machine_qemuppc ?= "1396a8f7b62f8926dc1fa474c7d94ae920d81852"
SRCREV_machine_qemux86 ?= "9d32bb075e349cc69c7af42b60f6715c5d8c972e"
SRCREV_machine_qemux86-64 ?= "dd488f551fa0f8e3bf1aadd78083b8547bba8bdf"
SRCREV_machine ?= "76133a1cadf0de417c29ed15d6fbb12c41c0802b"
SRCREV_meta ?= "ee78519365bdb25287703bbc31c06b193263c654"

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
