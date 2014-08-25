require recipes-kernel/linux/linux-yocto.inc

KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.14.13"

KMETA = "meta"

SRCREV_machine ?= "42477caf6bfda5ca7439121d348e474cff44ab5c"
SRCREV_meta ?= "ccad961c4cb6be245ed198bd2c17c27ab33cfcd7"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
