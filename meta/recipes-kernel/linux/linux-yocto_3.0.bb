inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.10"

SRCREV_machine_qemuarm ?= "42573a6932e65456471efa94d4863b8df88e48b3"
SRCREV_machine_qemumips ?= "55d4921f21dea9f07344029c12400bb3ef15d76b"
SRCREV_machine_qemuppc ?= "d10f26a8325a40610fc8d6a830a9f34dcb794075"
SRCREV_machine_qemux86 ?= "7814f075ea132cc17da89ecd6d886036e61e2187"
SRCREV_machine_qemux86-64 ?= "988fcbe64829f0a03ccfcc08d45cedb26cabe9ed"
SRCREV_machine ?= "ab1de8c21d2b1d084b8488496d75cc54fcd94f02"
SRCREV_meta ?= "67ce7623909cef63927fd145026aaf371cf4abf1"

PR = "r2"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
