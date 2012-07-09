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

SRCREV_machine_qemuarm ?= "93bb8cf97a0c5aead15a97c825d8fd504759f038"
SRCREV_machine_qemumips  ?= "b9b9aeb414c14f2587690d91052c9348e9ccd72a"
SRCREV_machine_qemuppc ?= "53ca8948fa64ee445001392a1cd03491befa9f35"
SRCREV_machine_qemux86 ?= "95c79fb2f965f1feb33bffe46c20ddb0b8e9da0d"
SRCREV_machine_qemux86-64 ?= "95c79fb2f965f1feb33bffe46c20ddb0b8e9da0d"
SRCREV_machine ?= "95c79fb2f965f1feb33bffe46c20ddb0b8e9da0d"
SRCREV_meta ?= "a8cf77018b0faa0d29f1483ff4e5a2034dc8edd5"

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
