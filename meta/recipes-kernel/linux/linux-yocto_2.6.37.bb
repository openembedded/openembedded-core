inherit kernel
require linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KBRANCH = ${KMACHINE}
KMETA = meta

LINUX_VERSION ?= "2.6.37"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE}"

SRCREV_machine_qemuarm = "92232beb4e72fcf8d85abe79edba0cdf53f1ce41"
SRCREV_machine_qemumips = "30cc7e06fba9126e79b4a0a3219c7d0259638f7a"
SRCREV_machine_qemuppc = "4ae67af1d0ece9944f8e640925aaf69aa5f10e34"
SRCREV_machine_qemux86 = "ea7bcd9e408ddfaf5ecd0bcc37956998add58e2d"
SRCREV_machine_qemux86-64 = "790b4cd5ff0bb8c4f4523763436efd438c692342"
SRCREV_machine = "bb8e31f2c99f5e007660d4540df246fb7ecfa746"
SRCREV_meta = "d5d3c6480d61f83503ccef7fbcd765f7aca8b71b"

PR = "r20"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-2.6.37;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

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
