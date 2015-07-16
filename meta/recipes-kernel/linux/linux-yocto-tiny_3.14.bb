KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "3.14.36"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "7534aeb01883b48cc42eb4900d0a8c64e8160e14"
SRCREV_meta ?= "94fa1d7e980c97fcd59b37daedcd863bd6daaee4"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
