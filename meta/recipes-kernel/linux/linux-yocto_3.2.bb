require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/default/base"
KBRANCH_qemux86  = "standard/default/common-pc/base"
KBRANCH_qemux86-64  = "standard/default/common-pc-64/base"
KBRANCH_qemuppc  = "standard/default/qemu-ppc32"
KBRANCH_qemumips = "standard/default/mti-malta32-be"
KBRANCH_qemuarm  = "standard/default/arm-versatile-926ejs"

LINUX_VERSION ?= "3.2.18"

SRCREV_machine_qemuarm ?= "2ea2477e2965569517671ec03fa9496214e8bff1"
SRCREV_machine_qemumips  ?= "17eb00dec08ef91acfb6d7adca81cb6e344b6c68"
SRCREV_machine_qemuppc ?= "14be92545abd339033570620e84ec2ae6780be29"
SRCREV_machine_qemux86 ?= "34e76349ed6cb7cadbbf94a5b34712d139703c8a"
SRCREV_machine_qemux86-64 ?= "f3625121d459b8e38f7528c2f5d2feb6078d0de9"
SRCREV_machine ?= "7cc31a952f78b8f8e8469eed93c23e9675a8eeb5"
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
