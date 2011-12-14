inherit kernel
require recipes-kernel/linux/linux-yocto.inc

#KMACHINE = "yocto/standard/tiny/base"
KMACHINE = "yocto/standard/base"
KBRANCH = ${KMACHINE}

LINUX_VERSION ?= "3.0.12"

SRCREV_machine ?= "f389d310965a56091f688b28ea8be6d9cbb7fbbe"
SRCREV_meta ?= "04a52a32cbdf0972033b97b83eaa83eb275dfdc9"

PR = "r1"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-3.0;protocol=git;nocheckout=1;branch=${KBRANCH},meta;name=machine,meta \
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
KERNEL_FEATURES=""
