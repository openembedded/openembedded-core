KBRANCH ?= "standard/tiny/common-pc"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.1.33"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "a38cb202738a2b055ac216b3699cc9377edea45a"
SRCREV_meta ?= "322fa5b2796bfcff7bfbbde1d76c73636ecf5857"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-4.1.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.1;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "(qemux86$)"

# Functionality flags
KERNEL_FEATURES = ""
