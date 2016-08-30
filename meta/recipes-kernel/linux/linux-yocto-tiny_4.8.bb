KBRANCH ?= "standard/tiny/common-pc"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.8-rc4"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "3eab887a55424fc2c27553b7bfe32330df83f7b8"
SRCREV_meta ?= "8cb7317502c2577f8c83eaf1c061603023824313"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.8.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.8;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "(qemux86$)"

# Functionality flags
KERNEL_FEATURES = ""
