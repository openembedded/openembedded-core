inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = "${KMACHINE}"

LINUX_VERSION ?= "3.0.24"

SRCREV_machine_qemuarm ?= "62bbe1d7a0e5c1200a99dabca889a52c9417b96b"
SRCREV_machine_qemumips ?= "66de88919f24f1e590a48dae6de752a68fed2353"
SRCREV_machine_qemuppc ?= "7528f1d06ef5665eed8c1498f62d5403b82bbbd6"
SRCREV_machine_qemux86 ?= "f153b0eb8264dc1e69f59d4c9173619feb4d5bd9"
SRCREV_machine_qemux86-64 ?= "aac580659dc0ce083f250fb05abf82e58d7f4531"
SRCREV_machine ?= "da7c40006b08916ff3a3db104def82aaf9ac2716"
SRCREV_meta ?= "34e0d2b4b4e9778b31f9ea99ca43f0dc71a7ee23"

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
