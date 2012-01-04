inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.14"

SRCREV_machine_qemuarm ?= "c498cc6e4b56784b3c2c547cb9a7af435b9412c1"
SRCREV_machine_qemumips ?= "791c3f4124a238a798e2360340800f4989fad695"
SRCREV_machine_qemuppc ?= "10e808d1c304b14cf42903fd637f239766b78476"
SRCREV_machine_qemux86 ?= "e04f9f8e564c60b1ce907d64074c18730f8dab4e"
SRCREV_machine_qemux86-64 ?= "53c2fa060d34e9a0b59e19398aeebbe73f24d89b"
SRCREV_machine ?= "fe80c1e343bf8f038328a612cef7f821d7ec8dbf"
SRCREV_meta ?= "73dafd44ea875df654129b32b2877f342d5573e4"

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
