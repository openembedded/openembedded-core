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

SRCREV_machine_qemuarm = "083e3befb5f2562e1375f19e645f1e024c92b0b2"
SRCREV_machine_qemumips = "79f46ece4d5e54fce49442f575a4b8663012dfa2"
SRCREV_machine_qemuppc = "f1ce5cf96659e036674eddd170633caaf3c1d487"
SRCREV_machine_qemux86 = "d44ab16a5c0aa5fed2ef2f587bd90cad37132dbd"
SRCREV_machine_qemux86-64 = "842be4e7221f982e0a76075cf556a9354991ff94"
SRCREV_machine = "66398ee1195e1b142a1b540795ff16e04b32467f"
SRCREV_meta = "d588bdafc0d9b4d2386144b7d76a1d379e2d16c0"

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
