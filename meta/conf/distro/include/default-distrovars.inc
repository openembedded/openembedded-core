QA_LOGFILE = "${TMPDIR}/qa.log"

IMAGE_ROOTFS_SIZE_ext2 ?= "131072"

OEINCLUDELOGS ?= "yes"
KERNEL_CONSOLE ?= "ttyS0"

require conf/distro/include/preferred-xorg-versions.inc

PCMCIA_MANAGER ?= "pcmciautils"

IMAGE_LINGUAS ?= "en-us en-gb"
LIMIT_BUILT_LOCALES ?= "POSIX en_US en_GB"
ENABLE_BINARY_LOCALE_GENERATION ?= "1"
LOCALE_UTF8_ONLY ?= "0"

DISTRO_FEATURES ?= "alsa bluetooth ext2 irda pcmcia usbgadget usbhost wifi nfs zeroconf pci"

DISTRO_EXTRA_RDEPENDS += "task-core-boot"
DISTRO_EXTRA_RRECOMMENDS += "kernel-module-af-packet"

IMAGE_FEATURES ?= ""

# This is a list of packages that are used by the build system to build the distribution, they are not
# directly part of the distribution. 
HOSTTOOLS_WHITELIST_GPLv3 ?= ""
WHITELIST_GPLv3 ?= "less"
LGPLv2_WHITELIST_GPLv3 ?= "libassuan gnutls libtasn1 libidn libgcc gcc-runtime" 

# This is a list of packages that require a commercial license to ship
# product. If shipped as part of an image these packages may have 
# implications so they are disabled by default
COMMERCIAL_LICENSE ?= "lame gst-fluendo-mp3 libmad mpeg2dec ffmpeg qmmp"
COMMERCIAL_AUDIO_PLUGINS ?= ""
# COMMERCIAL_AUDIO_PLUGINS ?= "gst-plugins-ugly-mad gst-plugins-ugly-mpegaudioparse"
COMMERCIAL_VIDEO_PLUGINS ?= ""
# COMMERCIAL_VIDEO_PLUGINS ?= "gst-plugins-ugly-mpeg2dec gst-plugins-ugly-mpegstream gst-plugins-bad-mpegvideoparse"
COMMERCIAL_QT ?= ""
# COMMERCIAL_QT ?= "qmmp"
# Set of common licenses used for license.bbclass
COMMON_LICENSE_DIR ??= "${COREBASE}/meta/files/common-licenses"

BB_GENERATE_MIRROR_TARBALLS ??= "0"
