inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.4"

SRCREV_machine_qemuarm ?= "b18f5c601ccc90b4c94d1d29282cf7c53fcb6e22"
SRCREV_machine_qemumips ?= "0696ab4056ddc1b4504e8636e47a918bdff5b257"
SRCREV_machine_qemuppc ?= "af58047d79790a06e5ac5ba09837c6e65220d7aa"
SRCREV_machine_qemux86 ?= "515e039eeb316494955e46d38c164e56999511ae"
SRCREV_machine_qemux86-64 ?= "d19118d19d5f95e83d6fbe1268fe6b84f67de6d3"
SRCREV_machine ?= "6fcf6f1bca9323fa2e9a5ea15fa84153cccbc28b"
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
