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

LINUX_VERSION ?= "3.2.8"


SRCREV_machine_qemuarm ?= "2fdc18ce2b9bf95519a02640cb45930ae7587dd4"
SRCREV_machine_qemumips ?= "2b2a6bad57fe403f1180c4b9021210da87d02c20"
SRCREV_machine_qemuppc ?= "eabeabc4c8a17822772895d3ba22989ae18a58c3"
SRCREV_machine_qemux86 ?= "ea5bd45e564520ca4ecdb10f7af63263d5fe33f7"
SRCREV_machine_qemux86-64 ?= "f54af84c64408a0259a919d1fd2405c0115c0a0b"
SRCREV_machine ?= "21ab5dca134a6bf1316aa59f69f9ee9e091d5702"
SRCREV_meta ?= "4c648bbbc7d2204d1b24f444aebb10f91c4ecae3"

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
