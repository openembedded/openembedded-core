inherit kernel
require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/base"
KBRANCH_qemux86  = "standard/common-pc/base"
KBRANCH_qemux86-64  = "standard/common-pc-64/base"
KBRANCH_qemuppc  = "standard/qemuppc"
KBRANCH_qemumips = "standard/mti-malta32"
KBRANCH_qemumips64 = "standard/mti-malta64"
KBRANCH_qemumips64el = "standard/mti-malta64"
KBRANCH_qemuarm  = "standard/arm-versatile-926ejs"

SRCREV_machine_qemuarm ?= "5a282e8b7138174389b64846ea993240c8b1f869"
SRCREV_machine_qemumips  ?= "7db36332a033ab89988dfcb7b563cbcd83b93663"
SRCREV_machine_qemuppc ?= "74f0e62044042a7e7abdbac83c6bf160b0c12c58"
SRCREV_machine_qemux86 ?= "aa9621f0955723c71821cc25e711a6d8fe80caf9"
SRCREV_machine_qemux86-64 ?= "aa9621f0955723c71821cc25e711a6d8fe80caf9"
SRCREV_machine ?= "aa9621f0955723c71821cc25e711a6d8fe80caf9"
SRCREV_meta ?= "ef03644fe33f7fd6f50a36b85701fdc6d73e2c96"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.4.git;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta"

LINUX_VERSION ?= "3.4.3"

PR = "r0"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMETA = "meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_REVISION_CHECKING=""
KERNEL_FEATURES="features/netfilter"
KERNEL_FEATURES_append_qemux86=" cfg/sound"
KERNEL_FEATURES_append_qemux86-64=" cfg/sound"
