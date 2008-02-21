require linux.inc
require linux-openmoko.inc

DESCRIPTION = "Linux 2.6.x kernel for FIC SmartPhones shipping w/ OpenMoko"
VANILLA_VERSION = "2.6.22"
KERNEL_RELEASE = "2.6.22.5"

# If you use a rc, you will need to use this:
#PV = "${VANILLA_VERSION}+${KERNEL_RELEASE}-moko11+svnr${SRCREV}"

PV = "${KERNEL_RELEASE}-moko11+svnr${SRCREV}"
PR = "r15"

KERNEL_IMAGETYPE = "uImage"
UBOOT_ENTRYPOINT = "30008000"

##############################################################
# source and patches
#
SRCREV_FORMAT = "patches"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/kernel/v2.6/linux-${VANILLA_VERSION}.tar.bz2 \
           ${KERNELORG_MIRROR}/pub/linux/kernel/v2.6/patch-${KERNEL_RELEASE}.bz2;patch=1 \
	       svn://svn.openmoko.org/trunk/src/target/kernel;module=patches;proto=http \
           file://fix-EVIOCGRAB-semantics-2.6.22.5.patch;patch=1 \
           file://fix-gta01-flowcontrol2-2.6.22.5.patch;patch=1 \
           file://gta-vibro-pwm-suspend.patch;patch=1 \
           file://tweak_power_button.patch;patch=1 \
	   file://break_suspend_cycle.patch;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/input_power-r9.patch;patch=1 \
           file://defconfig-${KERNEL_RELEASE}"

#           file://gta02-sound.patch;patch=1 \
#           file://soc-core-suspend.patch;patch=1 \
#           file://iis-suspend.patch;patch=1 \
#           file://s3c24xx-pcm-suspend.patch;patch=1 \

S = "${WORKDIR}/linux-${VANILLA_VERSION}"

##############################################################
# kernel image resides on a seperate flash partition (for now)
#
ALLOW_EMPTY = "1"

COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = 'fic-gta01|fic-gta02'

CMDLINE = "unused -- bootloader passes ATAG list"

###############################################################
# module configs specific to this kernel
#

# usb
module_autoload_ohci-hcd = "ohci-hcd"
module_autoload_hci_usb = "hci_usb"
module_autoload_g_ether = "g_ether"
# audio
module_autoload_snd-soc-neo1973-wm8753 = "snd-soc-neo1973-wm8753"
module_autoload_snd-soc-neo1973-gta02-wm8753 = "snd-soc-neo1973-gta02-wm8753"
module_autoload_snd-pcm-oss = "snd-pcm-oss"
module_autoload_snd-mixer-oss = "snd-mixer-oss"

# sd/mmc
module_autoload_s3cmci = "s3cmci"

do_prepatch() {
        mv ${WORKDIR}/patches ${S}/patches && cd ${S} && quilt push -av
        mv patches patches.openmoko
        mv .pc .pc.old
        mv ${WORKDIR}/defconfig-${KERNEL_RELEASE} ${WORKDIR}/defconfig
}

addtask prepatch after do_unpack before do_patch

