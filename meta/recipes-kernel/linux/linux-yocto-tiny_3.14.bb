require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.14"

KMETA = "meta"

SRCREV_machine ?= "0143c6ebb4a2d63b241df5f608b19f483f7eb9e0"
SRCREV_meta ?= "928d7b2ddad0abc8827055c51ba34225878344a0"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
