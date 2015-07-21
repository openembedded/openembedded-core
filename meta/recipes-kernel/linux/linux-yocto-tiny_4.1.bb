KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.1.2"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "4e30e64c44df9e59bd13239951bb8d2b5b276e6f"
SRCREV_meta ?= "45393dd54f5ad77d43014c407c2b3520da42f427"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
