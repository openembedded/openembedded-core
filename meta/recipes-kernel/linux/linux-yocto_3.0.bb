require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "yocto/standard/base"
KBRANCH_qemux86  = "yocto/standard/common-pc/base"
KBRANCH_qemux86-64  = "yocto/standard/common-pc-64/base"
KBRANCH_qemuppc  = "yocto/standard/qemu-ppc32"
KBRANCH_qemumips = "yocto/standard/mti-malta32-be"
KBRANCH_qemuarm  = "yocto/standard/arm-versatile-926ejs"

KMETA = "meta"

LINUX_VERSION ?= "3.0.32"

SRCREV_machine_qemuarm ?= "b8b6c3b6b3edf129ac26a27d779dd972c36e8ebd"
SRCREV_machine_qemumips ?= "33a643547e983adb442ff8e15645881816b17a7d"
SRCREV_machine_qemuppc ?= "bd9a3c4c066bd4b9f52b51aaaec9b029a7abe793"
SRCREV_machine_qemux86 ?= "70342faea067476774eb55f4e3098af0bcc48782"
SRCREV_machine_qemux86-64 ?= "cba836a545fbeb96f6f2392c3ecbac9d7735fa65"
SRCREV_machine ?= "bd6ad607c754dea30d91502a237870b4c45e0f1b"
SRCREV_meta ?= "d282029891bba5440630a885b940dea5e34e3e2c"

PR = "${INC_PR}.0"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"

# Functionality flags
KERNEL_FEATURES = "features/netfilter"
KERNEL_FEATURES_append = " features/taskstats"
KERNEL_FEATURES_append_qemux86 = " cfg/sound"
KERNEL_FEATURES_append_qemux86-64 = " cfg/sound"
