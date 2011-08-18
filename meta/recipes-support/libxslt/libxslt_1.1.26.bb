DESCRIPTION = "GNOME XSLT library"
HOMEPAGE = "http://xmlsoft.org/XSLT/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=0cd9a07afbeb24026c9b03aecfeba458"

SECTION = "libs"
DEPENDS = "libxml2"
PR = "r2"

SRC_URI = "ftp://xmlsoft.org/libxslt//libxslt-${PV}.tar.gz \
           file://pkgconfig_fix.patch"

SRC_URI[md5sum] = "e61d0364a30146aaa3001296f853b2b9"
SRC_URI[sha256sum] = "55dd52b42861f8a02989d701ef716d6280bfa02971e967c285016f99c66e3db1"
S = "${WORKDIR}/libxslt-${PV}"

inherit autotools pkgconfig binconfig lib_package

EXTRA_OECONF = "--without-python --without-debug --without-mem-debug --without-crypto"
# older versions of this recipe had ${PN}-utils
RPROVIDES_${PN}-bin += "${PN}-utils"
RCONFLICTS_${PN}-bin += "${PN}-utils"
RREPLACES_${PN}-bin += "${PN}-utils"

FILES_${PN}-dev += "${bindir}/xsltConf.sh"

BBCLASSEXTEND = "native"
