LICENSE = "GPL"
DESCRIPTION = "procfs tools"
SECTION = "x11"
PRIORITY = "optional"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DEPENDS = "gtk+"

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/pcmanfm-${PV}.tar.gz \
	   file://no-fam-gtk2.6.patch;patch=1;pnum=1"

inherit autotools pkgconfig

