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

SRCREV_machine_qemuarm ?= "cf6ee099f6de3a97968c052610f6e80a7efb6e55"
SRCREV_machine_qemumips ?= "ae0229025a5af8068fedb79e2a335ca35f8d9a6d"
SRCREV_machine_qemuppc ?= "8e792439aae9a95532f084badcb4574cb2d0818d"
SRCREV_machine_qemux86 ?= "5b48b18ce3055b5464f732ca0f9793a9ac596f7a"
SRCREV_machine_qemux86-64 ?= "050c0a7a6b1a412e75b907f0f61a125dea49af59"
SRCREV_machine ?= "030c87f1ffcbaecd0d06533fa1451c5333cd0bb0"
SRCREV_meta ?= "d386e09f316e03061c088d2b13a48605c20fb3a6"


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
