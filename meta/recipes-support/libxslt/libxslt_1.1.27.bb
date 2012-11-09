DESCRIPTION = "GNOME XSLT library"
HOMEPAGE = "http://xmlsoft.org/XSLT/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://Copyright;md5=0cd9a07afbeb24026c9b03aecfeba458"

SECTION = "libs"
DEPENDS = "libxml2"
PR = "r0"

SRC_URI = "ftp://xmlsoft.org/libxslt//libxslt-${PV}.tar.gz \
           file://pkgconfig_fix.patch"

SRC_URI[md5sum] = "4d01ff464b9ea7236925a1adf147c7f8"
SRC_URI[sha256sum] = "b6a8d9a421f2630999ca91d9f8c091ee8ea2d580e6be84c1d21b2a45e11e7e26"
S = "${WORKDIR}/libxslt-${PV}"

inherit autotools pkgconfig binconfig lib_package

# We don't DEPEND on binutils for ansidecl.h so ensure we don't use the header
do_configure_prepend () {
	sed -i -e 's/ansidecl.h//' ${S}/configure.in
}

EXTRA_OECONF = "--without-python --without-debug --without-mem-debug --without-crypto"
# older versions of this recipe had ${PN}-utils
RPROVIDES_${PN}-bin += "${PN}-utils"
RCONFLICTS_${PN}-bin += "${PN}-utils"
RREPLACES_${PN}-bin += "${PN}-utils"

FILES_${PN} += "${libdir}/libxslt-plugins"
FILES_${PN}-dev += "${libdir}/xsltConf.sh"

BBCLASSEXTEND = "native"
