require linux.inc
require linux-openmoko.inc

DESCRIPTION = "Linux 2.6.x (development) kernel for FIC SmartPhones shipping w/ Openmoko"

PE = "1"
PV = "${KERNEL_RELEASE}+git${SRCREV}"
PR = "r0"

SRC_URI = "git://git.openmoko.org/git/kernel.git;protocol=git;branch=stable"

S = "${WORKDIR}/git"

do_configure_prepend() {
        cp -f ${S}/defconfig-${CONFIG_NAME} ${WORKDIR}/defconfig
}

##############################################################
# kernel image resides on a seperate flash partition (for now)
#
ALLOW_EMPTY = "1"

CMDLINE = "unused -- bootloader passes ATAG list"

COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = "om-gta01|om-gta02"
DEFAULT_PREFERENCE = "1"

KERNEL_IMAGETYPE = "uImage"
KERNEL_RELEASE = "2.6.24"
KERNEL_VERSION = "${KERNEL_RELEASE}"

CONFIG_NAME_om-gta01 = "gta01"
CONFIG_NAME_om-gta02 = "gta02"

UBOOT_ENTRYPOINT = "30008000"

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
