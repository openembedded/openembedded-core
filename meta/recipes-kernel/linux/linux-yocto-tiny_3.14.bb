require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.14.13"

KMETA = "meta"

SRCREV_machine ?= "6cfe9c448741ab317d70a8ded3a953a0e66bb0a5"
SRCREV_meta ?= "3eefa4379f073768df150184e9dad1ff3228a0ff"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
