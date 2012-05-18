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

LINUX_VERSION ?= "3.2.11"

SRCREV_machine_qemuarm ?= "ba47a1cc9bb6ad576b2ac7adb3036bcfa569fe2e"
SRCREV_machine_qemumips ?= "3b2fd654392a2f33aed12748548c04e9b169591b"
SRCREV_machine_qemuppc ?= "cf3e188cf2a18c48a0e6f9ca54c36e6ac39512ec"
SRCREV_machine_qemux86 ?= "46f1007ad22b6790224c66a8dc4e80fdbd21eea1"
SRCREV_machine_qemux86-64 ?= "00e5ec2393bada6723bd9a07ded3d001c02fa727"
SRCREV_machine ?= "f4f8ba730e7783e09413872414d0a17c142c816d"
SRCREV_meta ?= "49f931bc294d5b6be60502bbd448cff5aa766235"

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

require linux-tools.inc
