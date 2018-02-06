KBRANCH ?= "v4.15/standard/tiny/common-pc"
LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "4.15"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "f7a6d45fff853173bfbf61706aeffcd1d1e99467"
SRCREV_meta ?= "ef2f5d9a0ac1c5ac60e76b18b0bb3393be450336"

PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;branch=${KBRANCH};name=machine \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-4.15;destsuffix=${KMETA}"

COMPATIBLE_MACHINE = "qemux86|qemux86-64"

# Functionality flags
KERNEL_FEATURES = ""

KERNEL_DEVICETREE_qemuarm = "versatile-pb.dtb"
