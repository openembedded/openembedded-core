inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KMACHINE = "common-pc"
KMACHINE_qemux86  = "common-pc"
KMACHINE_qemux86-64  = "common-pc-64"
KMACHINE_qemuppc  = "qemu-ppc32"
KMACHINE_qemumips = "mti-malta32-be"
KMACHINE_qemuarm  = "arm-versatile-926ejs"

KBRANCH = "standard/default/base"
KBRANCH_qemux86  = "standard/default/common-pc/base"
KBRANCH_qemux86-64  = "standard/default/common-pc-64/base"
KBRANCH_qemuppc  = "standard/default/qemu-ppc32"
KBRANCH_qemumips = "standard/default/mti-malta32-be"
KBRANCH_qemuarm  = "standard/default/arm-versatile-926ejs"

LINUX_VERSION ?= "3.2.2"

SRCREV_machine_qemuarm ?= "8d79190c307c7d41580beb77f83526ae3defcacc"
SRCREV_machine_qemumips ?= "c88f8c4f3be717be33a8ad89a14731eb94932336"
SRCREV_machine_qemuppc ?= "b8228f337002ad88f1e152d0c0c46c6035cd0428"
SRCREV_machine_qemux86 ?= "417fc778a86e81303bab5883b919ee422ec51c04"
SRCREV_machine_qemux86-64 ?= "417fc778a86e81303bab5883b919ee422ec51c04"
SRCREV_machine ?= "417fc778a86e81303bab5883b919ee422ec51c04"
SRCREV_meta ?= "138bf5b502607fe40315c0d76822318d77d97e01"

PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.2;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append=" features/taskstats"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"

require linux-tools.inc
