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

SRCREV_machine_qemuarm ?= "9aca8fec49787efbe44d0f137f31ee59edd94c49"
SRCREV_machine_qemumips  ?= "23fa942849b1207dbfc4528e2a5c8df0fd71870c"
SRCREV_machine_qemuppc ?= "08732f25bc3cb404e29ae201074bcf5c1661ea83"
SRCREV_machine_qemux86 ?= "6297e4c1d57e1063bfce297c2e12392348598559"
SRCREV_machine_qemux86-64 ?= "6297e4c1d57e1063bfce297c2e12392348598559"
SRCREV_machine ?= "6297e4c1d57e1063bfce297c2e12392348598559"
SRCREV_meta ?= "949fddda61053cc15e46c015e8f7db17e7b33360"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.4"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
