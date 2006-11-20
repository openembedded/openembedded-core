#@TYPE: Distribution
#@NAME: Angstrom 
#@DESCRIPTION: Distribution configuration for Angstrom

DISTRO = "angstrom"
DISTRO_NAME = "Angstrom"

TARGET_VENDOR = "-angstrom"
MACHINE_TASK_PROVIDER = "task-base"

# Can be "glibc" and "uclibc"
ANGSTROM_MODE ?= "glibc"

DEPLOY_DIR = "${TMPDIR}/deploy/${ANGSTROM_MODE}"
require conf/distro/include/angstrom-${ANGSTROM_MODE}.inc

#Use this variable in feeds and other parts that need a URI
ANGSTROM_URI = "http://www.angstrom-distribution.org"

#Set the default maintainer to angstrom-dev
MAINTAINER = "Angstrom Developers <angstrom-distro-devel@linuxtogo.org>"

#use ipkg package format with debian style naming
#use multimachine buildrules 
INHERIT += "package_ipk debian multimachine sanity"

#Generate locales on the buildsystem instead of on the target. Speeds up first boot, set to "1" to enable
PREFERRED_PROVIDER_qemu-native = "qemu-native"
ENABLE_BINARY_LOCALE_GENERATION ?= "1"

#qemu doesn't like armv6/eabi/vfp
ENABLE_BINARY_LOCALE_GENERATION_mx31ads = "0"

#ARM EABI is softfloat by default, but let's make sure :)
#make it overridable for platforms with FPU, like ep93xx or i.mx31
TARGET_FPU_arm ?= "soft"
TARGET_FPU_ixp4xx ?= "soft"


#Make a LE jffs2 image
SLUGOS_IMAGESEX = "little-endian"

#Name the generated images in a sane way
IMAGE_NAME = "${DISTRO_NAME}-${IMAGE_BASENAME}-${DISTRO_VERSION}-${MACHINE}"
DEPLOY_DIR_IMAGE = "${DEPLOY_DIR}/images/${MACHINE}"

# Angstrom *always* has some form of release config, so error out if someone thinks he knows better 
DISTRO_CHECK := "${@bb.data.getVar("DISTRO_VERSION",d,1) or bb.fatal('Remove this line or set a dummy DISTRO_VERSION if you really want to build an unversioned distro')}"

# We want images supporting the following features (for task-base)
DISTRO_FEATURES = "nfs smbfs wifi ppp alsa bluetooth ext2 irda pcmcia usbgadget usbhost"
