require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.14.5"

KMETA = "meta"

SRCREV_machine ?= "96930820e0cb6d4b31d5e0c8f3174805f4a868b3"
SRCREV_meta ?= "602be954ac45e8aea06cb93d6766bbf83c242090"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
