DESCRIPTION = "utilities for manipulating filesystem extended attributes"
HOMEPAGE = "http://savannah.nongnu.org/projects/attr/"
SECTION = "libs"

LICENSE = "LGPLv2.1+ & GPLv2+"
LICENSE_${PN} = "GPLv2+"
LICENSE_lib${PN} = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://doc/COPYING;md5=2d0aa14b3fce4694e4f615e30186335f \
                    file://attr/attr.c;endline=17;md5=be0403261f0847e5f43ed5b08d19593c \
                    file://libattr/libattr.c;endline=17;md5=7970f77049f8fa1199fff62a7ab724fb"

DEPENDS = "gettext"

SRC_URI = "http://nongnu.askapache.com/attr/${BPN}-${PV}.src.tar.gz"

inherit autotools

# the package comes with a custom config.h.in, it cannot be
# overwritten by autoheader
export AUTOHEADER = "true"
EXTRA_OECONF = "INSTALL_USER=root INSTALL_GROUP=root"

# absolute path will be used in ln -s if PKG_LIB_DIR and
# PKG_DEVLIB_DIR are different
EXTRA_OEMAKE = "PKG_LIB_DIR=${libdir} PKG_DEVLIB_DIR=${libdir}"

do_install () {
	oe_runmake install install-lib install-dev DIST_ROOT="${D}"
}

PACKAGES = "${PN} ${PN}-doc ${PN}-dbg ${PN}-locale lib${PN} lib${PN}-dev lib${PN}-doc"

FILES_${PN} = "${bindir}"

FILES_${PN}-dbg = "${bindir}/.debug"

FILES_${PN}-doc = "${docdir} \
                   ${mandir}/man1 \
                   ${mandir}/man5"

FILES_lib${PN} = "${libdir}/lib*.so.*"

FILES_lib${PN}-dev = "${includedir} \
                      ${libdir}/lib*.so \
                      ${libdir}/lib*.a \
                      ${libdir}/lib*.la"

FILES_lib${PN}-doc = "${mandir}/man2 \
                      ${mandir}/man3"

BBCLASSEXTEND = "native"
