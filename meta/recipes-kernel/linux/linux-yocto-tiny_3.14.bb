KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "3.14.36"

KMETA = "meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "4434aa71ff7043c570f9eae493df1ccadbda9b85"
SRCREV_meta ?= "162dfe3bb092c1a792e5ed224fe09672e9814b24"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.14.git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
