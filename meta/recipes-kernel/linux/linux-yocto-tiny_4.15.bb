KBRANCH ?= "v4.15/standard/tiny/common-pc"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.15.18"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "260fd9356917fb3bd73abd36a6bdd7df402ef1a1"
SRCREV_meta ?= "125963a34eebf4ee3d2b62d97c8090d912c8eb03"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.15;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "qemux86|qemux86-64"

# Functionality flags
KERNEL_FEATURES = ""

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"
