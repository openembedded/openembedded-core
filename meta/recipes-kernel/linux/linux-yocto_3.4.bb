require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemumipsel = "standard/mti-malta32"
KBRANCH_qemumips64 = "standard/mti-malta64"
KBRANCH_qemumips64el = "standard/mti-malta64"
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"

SRCREV_machine_qemuarm ?= "469cf6a66c720944adeaf8e43c044dfcb8d90599"
SRCREV_machine_qemumips  ?= "0985f3076c1a5a2cc3a01054e8a0411ffe440395"
SRCREV_machine_qemuppc ?= "e662bb66d9114c05919cd57ee4d620f4e67d2123"
SRCREV_machine_qemux86 ?= "f01bddc9534f31b27b475b68d13b5897533a6fca"
SRCREV_machine_qemux86-64 ?= "f01bddc9534f31b27b475b68d13b5897533a6fca"
SRCREV_machine ?= "f01bddc9534f31b27b475b68d13b5897533a6fca"
SRCREV_meta ?= "8d2012649516f3c489295a31fb9ea7afd1b464e4"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

SRC_URI += "file://noslang.patch"

LINUX_VERSION ?= "3.4.7"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
