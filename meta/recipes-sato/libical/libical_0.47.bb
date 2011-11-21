DESCRIPTION = "iCal and scheduling (RFC 2445, 2446, 2447) library"
HOMEPAGE = "http://sourceforge.net/projects/freeassociation/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=16077&atid=116077"
LICENSE = "LGPLv2.1 | MPL-1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d4fc58309d8ed46587ac63bb449d82f8 \
                    file://LICENSE;md5=d1a0891cd3e582b3e2ec8fe63badbbb6"
SECTION = "libs"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/freeassociation/${BPN}/${P}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "21f7f8a21e3d857c9476be732e52dc32"
SRC_URI[sha256sum] = "af4cbb4bb13d9ed3f2262181da9199823feba70802b15cc3e89b263d95da2888"

inherit autotools
