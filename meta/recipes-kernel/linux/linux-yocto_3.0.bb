inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = "${KMACHINE}"

LINUX_VERSION ?= "3.0.23"

SRCREV_machine_qemuarm ?= "927c56dcc7dc9199923a86cc0dac741b6b080953"
SRCREV_machine_qemumips ?= "b1f7eb611a30a46692c11ea1164d17f0d44e911b"
SRCREV_machine_qemuppc ?= "16f1ebf66ce2b58cbfc0ff7c8883c91c883e216a"
SRCREV_machine_qemux86 ?= "bc2cb6560d3dedf8337b5df8228a6e8002b6a98c"
SRCREV_machine_qemux86-64 ?= "b9cbe895ce0c259b4684355ea1602402b2653ca5"
SRCREV_machine ?= "8fd24b3570ab995848e4123ef13bac64e2c924be"
SRCREV_meta ?= "e559129b4a6f39f68b75141096b2d516cf7a7f35"

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
