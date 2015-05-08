KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "3.14.36"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "bda175966009d5a94103559e6e6ae51279952f39"
SRCREV_meta ?= "a996d95104b72c422a56e7d9bc8615ec4219ac74"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
