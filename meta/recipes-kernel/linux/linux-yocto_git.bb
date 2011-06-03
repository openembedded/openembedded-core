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

SRCREV_machine_qemuarm = "9192bec81b274a6bb71bcddcab9de5475f55ec15"
SRCREV_machine_qemumips = "3e57c1547b07b30900bb29fdedcc38e99c237446"
SRCREV_machine_qemuppc = "492cff27ae13c33fdab68f8d971b15dcb719e7bf"
SRCREV_machine_qemux86 = "ae10acfb83ed405127748393d9b4efb9e95cc78b"
SRCREV_machine_qemux86-64 = "28a3080512e19612a2e0b65d3077686f614ae556"
SRCREV_machine = "301ab1710d29a9bb931cb64c747d03da09dfea3f"
SRCREV_meta = "f1dc3722d45cdcc92c84ebfecf4ce616d2efed26"

PR = "r18"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-2.6.37;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

YOCTO_KERNEL_META_DATA=t

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout
addtask kernel_configcheck after do_configure before do_compile

require linux-tools.inc
