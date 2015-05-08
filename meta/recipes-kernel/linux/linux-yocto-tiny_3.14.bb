KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "3.14.36"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "b991a586470900e3de824f5ea75653aa2150594d"
SRCREV_meta ?= "46df2668fa657162d11f96a6f1af138c562a03aa"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
