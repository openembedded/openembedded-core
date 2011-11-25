inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "common-pc"
KMACHINE_qemux86  = "common-pc"
KMACHINE_qemux86-64  = "common-pc-64"
KMACHINE_qemuarm  = "arm-versatile-926ejs"
KMACHINE_qemuppc  = "qemu-ppc32"
KMACHINE_qemumips = "mti-malta32-be"

KBRANCH = "yocto/standard/preempt-rt/base"
KBRANCH_qemuppc = "yocto/standard/preempt-rt/qemu-ppc32"

LINUX_VERSION ?= "3.0.10"
LINUX_KERNEL_TYPE = "preempt-rt"

SRCREV_machine ?= "6196e0087445a92edf3c250983c5d320c668ae7c"
SRCREV_machine_qemuppc ?= "5a1d026aeba3c34d2895a5aeda6a1479bbdf62fb"
SRCREV_meta ?= "d386e09f316e03061c088d2b13a48605c20fb3a6"

PR = "r2"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0-1.1.x.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

# Omit broken machines from COMPATIBLE_MACHINE
#   qemuppc hangs at boot
#   qemumips panics at boot
COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm)"

# Functionality flags
KERNEL_FEATURES=features/netfilter
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require recipes-kernel/linux/linux-tools.inc
