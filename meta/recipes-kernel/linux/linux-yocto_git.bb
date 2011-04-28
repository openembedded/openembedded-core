inherit kernel
require linux-yocto.inc

KMACHINE = "yocto/standard/base"
KMACHINE_qemux86  = "yocto/standard/common-pc/base"
KMACHINE_qemux86-64  = "yocto/standard/common-pc-64/base"
KMACHINE_qemuppc  = "yocto/standard/qemu-ppc32"
KMACHINE_qemumips = "yocto/standard/mti-malta32-be"
KMACHINE_qemuarm  = "yocto/standard/arm-versatile-926ejs"
KMACHINE_atom-pc  = "yocto/standard/common-pc/atom-pc"
KMACHINE_routerstationpro = "yocto/standard/routerstationpro"
KMACHINE_mpc8315e-rdb = "yocto/standard/fsl-mpc8315e-rdb"
KMACHINE_beagleboard = "yocto/standard/beagleboard"

KBRANCH = ${KMACHINE}
KMETA = meta

LINUX_VERSION ?= "2.6.37"
LINUX_VERSION_EXTENSION ?= "-yocto-${LINUX_KERNEL_TYPE}"

SRCREV_machine_qemuarm = "e5ca41def834db9d18b28393a45d53a8d18f3d05"
SRCREV_machine_qemumips = "9bbc8e3432406429923fab0de038af5d3e647901"
SRCREV_machine_qemuppc = "f0ff494e62bfaa921e844c1ec7fe6cf4a977980a"
SRCREV_machine_qemux86 = "cb0537a40dadea20f12bc10e0986fd2a70290b42"
SRCREV_machine_qemux86-64 = "69cfbdf9f1ff461a75e5b77d6d7ba35e97db4cc3"
SRCREV_machine_emenlow = "2215a346eb4f9e09053d00bdf61ed999ff80e029"
SRCREV_machine_atom-pc = "69cfbdf9f1ff461a75e5b77d6d7ba35e97db4cc3"
SRCREV_machine_routerstationpro = "8db69980d76e1f863af409e70175963f23de83ab"
SRCREV_machine_mpc8315e-rdb = "6861d8a4d58f8aa75997f7678cc454791544507a"
SRCREV_machine_beagleboard = "69cfbdf9f1ff461a75e5b77d6d7ba35e97db4cc3"
SRCREV_machine = "69cfbdf9f1ff461a75e5b77d6d7ba35e97db4cc3"
SRCREV_meta = "ecab1e2bc12a8b0c4d064a00acc3260f6e8528c5"

PR = "r16"
PV = "${LINUX_VERSION}+git${SRCPV}"
SRCREV_FORMAT = "meta_machine"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-2.6.37;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64|mpc8315e-rdb|routerstationpro|beagleboard)"

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
