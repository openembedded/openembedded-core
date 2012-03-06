inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = "${KMACHINE}"

LINUX_VERSION ?= "3.0.22"

SRCREV_machine_qemuarm ?= "6d4d3334bf648b97a0d44b7833060f4da40840d5"
SRCREV_machine_qemumips ?= "1d8d58d120c92510be8a97b0ed493a25a3010d6c"
SRCREV_machine_qemuppc ?= "804aff4612bb7a53ac4679412c2a78bc06e3c4ce"
SRCREV_machine_qemux86 ?= "7b0476fde681e828417d8fa86c47b0ca539ade70"
SRCREV_machine_qemux86-64 ?= "14fe1742198bd5a85fbcf6e53d336838ff87b2ef"
SRCREV_machine ?= "c578f3a1f357142a6e02a1df4ae9aa16f45094d6"
SRCREV_meta ?= "d7b46785d6781e2c23d754f35f09bebd3b74b5bb"

PR = "r4"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES = "features/netfilter"
KERNEL_FEATURES_append = " features/taskstats"
KERNEL_FEATURES_append_qemux86 = " cfg/sound"
KERNEL_FEATURES_append_qemux86-64 = " cfg/sound"

require linux-tools.inc
