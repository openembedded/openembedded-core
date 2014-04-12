require recipes-kernel/linux/linux-yocto.inc

KBRANCH = "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

LINUX_VERSION ?= "3.14"

KMETA = "meta"

SRCREV_machine ?= "144595ef6215a0febfb8ee7d0c9e4eb2eaf93d61"
SRCREV_meta ?= "09424cee646626c04105f08455a58fabb27eff31"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
