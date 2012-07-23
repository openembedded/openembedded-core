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

SRCREV_machine_qemuarm ?= "b57548a29e44655e27cfbdf3a0642d682401b835"
SRCREV_machine_qemumips  ?= "633d487b0f6e5d328400b47f0d69d9dac7354c84"
SRCREV_machine_qemuppc ?= "6229fc0f62283e0dc9f8b5d2be01b048a92867f3"
SRCREV_machine_qemux86 ?= "19f7e43b54aef08d58135ed2a897d77b624b320a"
SRCREV_machine_qemux86-64 ?= "19f7e43b54aef08d58135ed2a897d77b624b320a"
SRCREV_machine ?= "19f7e43b54aef08d58135ed2a897d77b624b320a"
SRCREV_meta ?= "d7a96809a585e06933d8c08adb9b9f66b21efb4c"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.6"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
