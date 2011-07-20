inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "common-pc"
KMACHINE_qemux86  = "common-pc"
KMACHINE_qemux86-64  = "common-pc-64"
KMACHINE_qemuarm  = "arm-versatile-926ejs"
KMACHINE_qemuppc  = "qemu-ppc32"
KMACHINE_qemumips = "mti-malta32-be"

KBRANCH = "yocto/standard/preempt-rt/base"
KMETA = meta

LINUX_VERSION ?= "3.0"
LINUX_KERNEL_TYPE = "preempt-rt"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE}"

SRCREV_machine = "7e1e5b6c8a13c615feb0d7b6d37988a094aae98f"
SRCREV_meta = "7782fc673afd6a1c742409896ae787560d96fce8"

PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

# Omit broken machines from COMPATIBLE_MACHINE
#   qemuppc hangs at boot
#   qemumips panics at boot
COMPATIBLE_MACHINE = "(qemux86|qemux86-64|qemuarm)"

# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES=features/netfilter
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

YOCTO_KERNEL_META_DATA=t

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout
addtask kernel_configcheck after do_configure before do_compile

require recipes-kernel/linux/linux-tools.inc
