KBRANCH ?= "standard/tiny/base"

require recipes-kernel/linux/linux-yocto.inc

LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.10.62"

KMETA = "meta"

SRCREV_machine ?= "b2ac933df119a3444a32fcccf5e4ad453f5ac89d"
SRCREV_meta ?= "f4ab00d96a3d8e443d7f7744ad996e184eac03b5"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.10.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
