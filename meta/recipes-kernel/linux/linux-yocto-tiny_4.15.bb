KBRANCH ?= "v4.15/standard/tiny/common-pc"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.15.18"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "51273ff79f4ad930f4a788257f58e1266cb236e3"
SRCREV_meta ?= "9bbc5bd75e5322bd80769802c5062b2f3f0d6c7d"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.15;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "qemux86|qemux86-64"

# Functionality flags
KERNEL_FEATURES = ""

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"
