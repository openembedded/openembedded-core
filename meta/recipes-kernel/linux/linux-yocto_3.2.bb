inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "common-pc"
KMACHINE_qemux86  = "common-pc"
KMACHINE_qemux86-64  = "common-pc-64"
KMACHINE_qemuppc  = "qemu-ppc32"
KMACHINE_qemumips = "mti-malta32-be"
KMACHINE_qemuarm  = "arm-versatile-926ejs"

KBRANCH = "standard/default/base"
KBRANCH_qemux86  = "standard/default/common-pc/base"
KBRANCH_qemux86-64  = "standard/default/common-pc-64/base"
KBRANCH_qemuppc  = "standard/default/qemu-ppc32"
KBRANCH_qemumips = "standard/default/mti-malta32-be"
KBRANCH_qemuarm  = "standard/default/arm-versatile-926ejs"

LINUX_VERSION ?= "3.2.7"


SRCREV_machine_qemuarm ?= "c2028a144fe035719af7c5e9989fedc62ccf3c2c"
SRCREV_machine_qemumips ?= "7dfffd937d4755cba0fcf3f2b9b69a1c62262084"
SRCREV_machine_qemuppc ?= "8fc0489fffe4c626f9a8053ad86014e75073a3d0"
SRCREV_machine_qemux86 ?= "0fae9c2722039df3e93398d314ee10e3f4330b86"
SRCREV_machine_qemux86-64 ?= "0fae9c2722039df3e93398d314ee10e3f4330b86"
SRCREV_machine ?= "0fae9c2722039df3e93398d314ee10e3f4330b86"
SRCREV_meta ?= "5db8963f9aefc70b4483ca4ccce8f2ef6964e2c3"

PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
