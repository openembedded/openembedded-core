require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.14.13"

KMETA = "meta"

SRCREV_machine ?= "d61940e2aaeeedc43f166c9e7ac763461f380cd2"
SRCREV_meta ?= "3eefa4379f073768df150184e9dad1ff3228a0ff"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
