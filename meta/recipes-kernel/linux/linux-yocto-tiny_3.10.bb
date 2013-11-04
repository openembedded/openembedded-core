require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/tiny/base"
KBRANCH = "${KBRANCH_DEFAULT}"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.10.17"

KMETA = "meta"

SRCREV_machine ?= "375aee37c2508899b6a8c0bdff7d4d67cb75fb36"
SRCREV_meta ?= "f1c9080cd27f99700fa59b5375d1ddd0afe625ad"


PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
