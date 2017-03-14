KBRANCH ?= "standard/tiny/common-pc"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.4.53"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "cebe1ad56aebd89e0de29412e19433fb441bf13c"
SRCREV_meta ?= "864bc5736aa58c510713e7b76d205d5341377972"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.4.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.4;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "(qemux86$)"

# Functionality flags
KERNEL_FEATURES = ""
