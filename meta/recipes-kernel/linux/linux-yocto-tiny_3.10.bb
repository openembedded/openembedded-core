KBRANCH ?= "standard/tiny/base"

require recipes-kernel/linux/linux-yocto.inc

LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.10.54"

KMETA = "meta"

SRCREV_machine ?= "e001d3cbec5320c03d2a316aa0e287012be19133"
SRCREV_meta ?= "5ea3fd0817c2a3173e85102c61c51a5a6268cad6"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
