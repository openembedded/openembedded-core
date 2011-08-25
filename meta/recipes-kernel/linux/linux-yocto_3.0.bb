inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.3"

SRCREV_machine_qemuarm ?= "eecaa32095ab63e0a73f161cf48fd02251eb6c88"
SRCREV_machine_qemumips ?= "eee3cb2066bb6aae5b3869083cb477b03eb87c79"
SRCREV_machine_qemuppc ?= "c0da1d35dee9fb63ba6bfc7907a77b0feb89766e"
SRCREV_machine_qemux86 ?= "8b51ddba0f85a6bb8e105b37a9ac03fb58b9de20"
SRCREV_machine_qemux86-64 ?= "965dd365cf3652347960f7145ac188c86c323070"
SRCREV_machine ?= "49d93fb0c9e1645ba0bb8951be23e56ea227a5bd"
SRCREV_meta ?= "5b535279e61197cb194bb2dfceb8b7a04128387c"

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout
addtask kernel_configcheck after do_configure before do_compile

require linux-tools.inc
