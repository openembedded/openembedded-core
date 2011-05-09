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

SRCREV_machine_qemuarm = "b0375c21e29453458f9aa9986dc3f1ec69bf1aef"
SRCREV_machine_qemumips = "f5d26f2eda2be8b942172cda8f27de33ebf38ec3"
SRCREV_machine_qemuppc = "7eb6c68d977d9039a2b5a734172b064a9d19cdc1"
SRCREV_machine_qemux86 = "ad62d1aab734513cd96c8f4517f816420a218e77"
SRCREV_machine_qemux86-64 = "b906f358fd404a1e74a961f25079274e0d933ee1"
SRCREV_machine = "b906f358fd404a1e74a961f25079274e0d933ee1"
SRCREV_meta = "ecab1e2bc12a8b0c4d064a00acc3260f6e8528c5"

PR = "r17"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-2.6.37;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING ?= "t"
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

# extra tasks
addtask kernel_link_vmlinux after do_compile before do_install
addtask validate_branches before do_patch after do_kernel_checkout
addtask kernel_configcheck after do_configure before do_compile

require linux-tools.inc
