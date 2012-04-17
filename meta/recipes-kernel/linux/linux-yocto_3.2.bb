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

SRCREV_machine_qemuarm ?= "dfbba772cbee84125ac1558c904a7dc181445f5f"
SRCREV_machine_qemumips ?= "0e0c67635b74199d534f75500e5c1654a1219bc6"
SRCREV_machine_qemuppc ?= "92de4e2f3c6b397c8b363e079cc4d5e9bcadf877"
SRCREV_machine_qemux86 ?= "8ada1bb97415fe959a57a08504be4eb8a656ed30"
SRCREV_machine_qemux86-64 ?= "4ca7e2c5d42e755e1b4c3e1478128f047a8ed2a8"
SRCREV_machine ?= "01e948c2bdf7f5ad9f2b30047a8d3493a1a2880a"
SRCREV_meta ?= "b14a08f5c7b469a5077c10942f4e1aec171faa9d"

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
