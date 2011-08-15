inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}
KMETA = meta

LINUX_VERSION ?= "3.0.1"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE}"

SRCREV_machine_qemuarm = "36b4cdddcafc711f0ec9ad97882f23a6443c61b2"
SRCREV_machine_qemumips = "2db2aacc589a518a74e8a8543e80403f09de8134"
SRCREV_machine_qemuppc = "c0f5dd4ef37b445d8ac73b11dd93e0ffdb7e9f6d"
SRCREV_machine_qemux86 = "c29beadfd4364aca9358ac0fdb8356c1dbbdacb2"
SRCREV_machine_qemux86-64 = "d0b814813bce09971c5cbb0d22bacb8842146fa0"
SRCREV_machine = "ad13129a87957656c18584793abcee51de0e196a"
SRCREV_meta = "9da70812ecddee0f7eeb11675f29497cb997275e"

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

YOCTO_KERNEL_META_DATA=t

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout
addtask kernel_configcheck after do_configure before do_compile

require linux-tools.inc
