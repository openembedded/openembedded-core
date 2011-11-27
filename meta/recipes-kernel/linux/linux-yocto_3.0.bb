inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.4"

SRCREV_machine_qemuarm ?= "7908f38ac44359d58c40b166dbb45e48fc58295c"
SRCREV_machine_qemumips ?= "7ea75f58d69293e6b1c2f904f8f5790521a7ccee"
SRCREV_machine_qemuppc ?= "eccd57eaa4c2b580b9adbbc39e19ecbff56779ae"
SRCREV_machine_qemux86 ?= "72671808fdbe69a9fe03fd8f094e7c59da04a28c"
SRCREV_machine_qemux86-64 ?= "2b2d0954a6fd12b4bb7f02f019bc62633c8060a1"
SRCREV_machine ?= "6b2c7d65b844e686eae7d5cccb9b638887afe28e"
SRCREV_meta ?= "d05450e4aef02c1b7137398ab3a9f8f96da74f52"

PR = "r3"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0-1.1.x.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
