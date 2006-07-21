DESCRIPTION = "Zile is a very small emacs-like editor."
HOMEPAGE = "http://zile.sourceforge.net/"
LICENSE = "GPL"
DEPENDS = "ncurses"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"
PRIORITY = "optional"
SECTION = "console/editors"
PR = "r1"

UV = "${@bb.data.getVar('PV', d, 1).split('+')[1]}"

SRC_URI = "${SOURCEFORGE_MIRROR}/zile/zile-${UV}.tar.gz \
	   file://for_build.patch;patch=1"
S = "${WORKDIR}/zile-${UV}"

inherit autotools

export CC_FOR_BUILD = "${BUILD_CC}"
export CFLAGS_FOR_BUILD = "${BUILD_CFLAGS} -DHAVE_VASPRINTF"
export LDFLAGS_FOR_BUILD = "${BUILD_LDFLAGS}"
