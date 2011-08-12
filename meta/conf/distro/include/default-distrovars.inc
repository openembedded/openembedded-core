QA_LOGFILE = "${TMPDIR}/qa.log"

OEINCLUDELOGS ?= "yes"
KERNEL_CONSOLE ?= "ttyS0"

PCMCIA_MANAGER ?= "pcmciautils"

IMAGE_LINGUAS ?= "en-us en-gb"
LIMIT_BUILT_LOCALES ?= "POSIX en_US en_GB"
ENABLE_BINARY_LOCALE_GENERATION ?= "1"
LOCALE_UTF8_ONLY ?= "0"

DISTRO_FEATURES_LIBC ?= "ipv4 ipv6 libc-backtrace libc-big-macros libc-bsd libc-cxx-tests libc-catgets libc-charsets libc-crypt \
					libc-crypt-ufc libc-db-aliases libc-envz libc-fcvt libc-fmtmsg libc-fstab libc-ftraverse \
					libc-getlogin libc-idn libc-inet libc-inet-anl libc-libm libc-libm-big libc-locales libc-locale-code \
					libc-memusage libc-nis libc-nsswitch libc-rcmd libc-rtld-debug libc-spawn libc-streams libc-sunrpc \
					libc-utmp libc-utmpx libc-wordexp libc-posix-clang-wchar libc-posix-regexp libc-posix-regexp-glibc \
					libc-posix-wchar-io"
DISTRO_FEATURES ?= "alsa argp bluetooth ext2 irda largefile pcmcia usbgadget usbhost wifi xattr nfs zeroconf pci 3g x11 ${DISTRO_FEATURES_LIBC}"

IMAGE_FEATURES ?= ""

# This is a list of packages that are used by the build system to build the distribution, they are not
# directly part of the distribution. 
HOSTTOOLS_WHITELIST_GPLv3 ?= ""
WHITELIST_GPLv3 ?= "less"
LGPLv2_WHITELIST_GPLv3 ?= "libassuan gnutls libtasn1 libidn libgcc gcc-runtime" 

# This is a list of packages that require a commercial license to ship
# product. If shipped as part of an image these packages may have 
# implications so they are disabled by default
COMMERCIAL_LICENSE ?= "lame gst-fluendo-mp3 libmad mpeg2dec ffmpeg qmmp ${COMMERCIAL_LICENSE_DEPENDEES}"
COMMERCIAL_LICENSE_DEPENDEES ?= "gst-plugins-ugly libomxil gst-openmax"
COMMERCIAL_AUDIO_PLUGINS ?= ""
# COMMERCIAL_AUDIO_PLUGINS ?= "gst-plugins-ugly-mad gst-plugins-ugly-mpegaudioparse"
COMMERCIAL_VIDEO_PLUGINS ?= ""
# COMMERCIAL_VIDEO_PLUGINS ?= "gst-plugins-ugly-mpeg2dec gst-plugins-ugly-mpegstream gst-plugins-bad-mpegvideoparse"
COMMERCIAL_QT ?= ""
# COMMERCIAL_QT ?= "qmmp"
# Set of common licenses used for license.bbclass
COMMON_LICENSE_DIR ??= "${COREBASE}/meta/files/common-licenses"

BB_GENERATE_MIRROR_TARBALLS ??= "0"

NO32LIBS ??= "1"

# Default to emitting logfiles if a build fails.
BBINCLUDELOGS ??= "yes"

