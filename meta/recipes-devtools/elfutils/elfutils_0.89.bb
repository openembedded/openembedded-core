SECTION = "base"
LICENSE = "OSL"
DESCRIPTION = "A collection of utilities and DSOs to handle compiled objects."
DEPENDS = "libtool"

SRC_URI = "http://distro.ibiblio.org/pub/linux/distributions/gentoo/distfiles/elfutils-${PV}.tar.gz \
	   file://warnings.patch;patch=1"

inherit autotools

