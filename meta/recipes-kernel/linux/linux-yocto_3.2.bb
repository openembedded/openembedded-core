require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/default/base"
KBRANCH_qemux86  = "standard/default/common-pc/base"
KBRANCH_qemux86-64  = "standard/default/common-pc-64/base"
KBRANCH_qemuppc  = "standard/default/qemu-ppc32"
KBRANCH_qemumips = "standard/default/mti-malta32-be"
KBRANCH_qemuarm  = "standard/default/arm-versatile-926ejs"

LINUX_VERSION ?= "3.2.18"

SRCREV_machine_qemuarm ?= "c721e94f7cd1b1ae7da5edc52076c771b2e9d134"
SRCREV_machine_qemumips  ?= "2172812072c372cc704dac5e1654da81d6eabad3"
SRCREV_machine_qemuppc ?= "800615ccdce45f6900b841954fa2f47987bb2a95"
SRCREV_machine_qemux86 ?= "9e23973679d82a54dda697f539243fdea612e878"
SRCREV_machine_qemux86-64 ?= "d7823aebcf7f724f50e30f245e5e71b3b4db1a0b"
SRCREV_machine ?= "06882fc16a4e965872e7faacb91da1497efd9ac3"
SRCREV_meta ?= "486f7aec824b4127e91ef53228823e996b3696f0"

PR = "${INC_PR}.1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "qemuarm|qemux86|qemuppc|qemumips|qemux86-64"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
