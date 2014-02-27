require recipes-kernel/linux/linux-yocto.inc

KBRANCH_DEFAULT = "standard/tiny/base"
KBRANCH = "${KBRANCH_DEFAULT}"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.10.32"

KMETA = "meta"

SRCREV_machine ?= "78afd3095c9b37efbbfbfdc25eb3833ef3c6a718"
SRCREV_meta ?= "7b3b87d4d5e4c41c235da13aaa9f45d5d338e2c6"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
