KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "3.14.29"

KMETA = "meta"

SRCREV_machine ?= "21ba402e0a5286baf0b8960da132e2d56f020c88"
SRCREV_meta ?= "6eddbf47875ef48ddc5864957a7b63363100782b"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
