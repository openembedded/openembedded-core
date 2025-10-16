KBRANCH ?= "v6.6/standard/tiny/base"

LINUX_KERNEL_TYPE = "tiny"
KCONFIG_MODE = "--allnoconfig"

require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion_6.6.inc

LINUX_VERSION ?= "6.6.109"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

DEPENDS += "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"

KMETA = "kernel-meta"
KCONF_BSP_AUDIT_LEVEL = "2"

SRCREV_machine ?= "b481c2fca6f1528d9bfe55daffb89d380e941335"
SRCREV_meta ?= "aef3b17fc578bf09fc3b27e4eb45a7ce7a1b39d8"

PV = "${LINUX_VERSION}+git"

SRC_URI = "git://git.yoctoproject.org/linux-yocto.git;branch=${KBRANCH};name=machine;protocol=https \
           git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=${KMETA};protocol=https"

COMPATIBLE_MACHINE = "^(qemux86|qemux86-64|qemuarm64|qemuarm|qemuarmv5)$"

# Functionality flags
KERNEL_FEATURES = ""

KERNEL_DEVICETREE:qemuarmv5 = "arm/versatile-pb.dtb"
