inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.18"

SRCREV_machine_qemuarm ?= "1f3be2c26b6b79cc7c705a033816940a94e89936"
SRCREV_machine_qemumips ?= "be915b8e22e83741425a8a82f4886ef31e181d89"
SRCREV_machine_qemuppc ?= "002b274d2dd8be5b1ade2152d5180f7da773bdad"
SRCREV_machine_qemux86 ?= "d7e81e7f975c57c581ce13446adf023f95d9fd9f"
SRCREV_machine_qemux86-64 ?= "165ee6a9b142853b5f8e9e94613ed9c0db3ec27d"
SRCREV_machine ?= "70ecfb7613ba70f0f8f46cea27b5f9065ad11023"
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
