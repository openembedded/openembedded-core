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

LINUX_VERSION ?= "3.0"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE}"

SRCREV_machine_qemuarm = "30a2cec4e9d7048681c1d239f49cc935287a7012"
SRCREV_machine_qemumips = "f9b9688cefaa87d898394902acfb858d6b0f1e28"
SRCREV_machine_qemuppc = "a46a4c356f8bcf7a30d967a1b43633b824cd07a1"
SRCREV_machine_qemux86 = "efaab1f75dbc577922c862fa19d45c3f31cb99cb"
SRCREV_machine_qemux86-64 = "c30a56850c73c74af2d056d803081a0d829ce35c"
SRCREV_machine = "318b059a472128f67119f367f51f11893ee60269"
SRCREV_meta = "76b3679beff6c640388a609ccf87f46bb7bf0f2b"

PR = "r0"
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
