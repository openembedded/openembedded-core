DESCRIPTION = "iCal and scheduling (RFC 2445, 2446, 2447) library"
HOMEPAGE = "http://sourceforge.net/projects/freeassociation/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=16077&atid=116077"
LICENSE = "LGPLv2.1 | MPL-1"
LIC_FILES_CHKSUM = "file://COPYING;md5=35da21efce2b9f0ca07524c9f844e6ed \
			file://LICENSE;md5=35da21efce2b9f0ca07524c9f844e6ed"
SECTION = "libs"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/freeassociation/${P}.tar.gz"

SRC_URI[md5sum] = "9c08f88945bfd5d0791d102e4aa4125c"
SRC_URI[sha256sum] = "000762efb81501421d94ec56343648a62b3bd2884e7cdf6f638e2c207dd8f6a1"

inherit autotools
