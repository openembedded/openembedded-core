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

SRCREV_machine_qemuarm ?= "48e68bd72da497a14200801140b3b87dae6d60e9"
SRCREV_machine_qemumips ?= "9d86aeb43e5bbb0851cc4f105b64caab13bc71f3"
SRCREV_machine_qemuppc ?= "15fd748017f0849138ff4b47d73f6866fa26cfe8"
SRCREV_machine_qemux86 ?= "8f74a4339b3dc029fafff0ba7d88d6dc950d4b31"
SRCREV_machine_qemux86-64 ?= "610c5a62daeda033755b0b7bfcb3e2cad5c76f3f"
SRCREV_machine ?= "5df0b4c2538399aed543133b3855f809adf08ab8"
SRCREV_meta ?= "77ca4855e80acb8dad21acea946908716c308b5b"

PR = "r3"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
