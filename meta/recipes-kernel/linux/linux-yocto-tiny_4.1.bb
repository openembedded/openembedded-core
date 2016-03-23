KBRANCH ?= "standard/tiny/base"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.1.18"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "f753cbe35057d5df18bcf1adafd74e64dbbdff6b"
SRCREV_meta ?= "b9023d4c8fbbb854c26f158a079a5f54dd61964d"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
