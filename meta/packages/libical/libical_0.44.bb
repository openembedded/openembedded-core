DESCRIPTION = "iCal and scheduling (RFC 2445, 2446, 2447) library"
HOMEPAGE = "http://sourceforge.net/projects/freeassociation/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=16077&atid=116077"
LICENSE = "LGPLv2.1 | MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=35da21efce2b9f0ca07524c9f844e6ed \
			file://LICENSE;md5=35da21efce2b9f0ca07524c9f844e6ed"
SECTION = "libs"

SRC_URI = "${SOURCEFORGE_MIRROR}/freeassociation/${P}.tar.gz"

inherit autotools
