inherit kernel
require recipes-kernel/linux/linux-yocto.inc

# We need lzma (as CONFIG_KERNEL_LZMA=y)
DEPENDS += "xz-native"

#KMACHINE = "yocto/standard/tiny/base"
KMACHINE = "yocto/standard/base"
KBRANCH = "${KMACHINE}"

LINUX_VERSION ?= "3.0.24"

SRCREV_machine ?= "da7c40006b08916ff3a3db104def82aaf9ac2716"
SRCREV_meta ?= "a4ac64fe873f08ef718e2849b88914725dc99c1c"

PR = "r3"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;bareclone=1;branch=${KBRANCH},meta;name=machine,meta \
           file://core.cfg \
           file://serial.cfg \
           file://ext2.cfg \
           file://rtc-pc.cfg \
           file://ramfs.cfg \
           file://devtmpfs.cfg \
           file://net.cfg \
           file://debug.cfg \
           file://lzma.cfg \
           "


# Enable qemux86 specific emulated options
SRC_URI_append_qemux86 = " file://defconfig file://ata.cfg file://e1000.cfg"

#COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64)"
COMPATIBLE_MACHINE = "(qemux86)"

# Functionality flags
KERNEL_FEATURES = ""
