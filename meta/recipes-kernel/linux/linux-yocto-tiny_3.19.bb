KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "3.19.5"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "d3c0b958e68824c7037005e2a84e5661ba98d5b0"
SRCREV_meta ?= "727cfce45077a1f73f1dbc42e8093fde0c5e36a7"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.19.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
