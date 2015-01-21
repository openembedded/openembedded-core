KBRANCH ?= "standard/tiny/base"

require recipes-kernel/linux/linux-yocto.inc

LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.10.65"

KMETA = "meta"

SRCREV_machine ?= "a2f2be49cd60b8d022fa47daae0a8293c3066b78"
SRCREV_meta ?= "d5456dd830cad14bd844753b751b83744ced3793"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
