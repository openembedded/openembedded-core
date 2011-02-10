SUMMARY = "Tools for working with DSSSL stylesheets for SGML and XML documents"
DESCRIPTION = "OpenJade is a suite of tools for validating, \
processing, and applying DSSSL (Document Style Semantics and \
Specification Language) stylesheets to SGML and XML documents."
HOMEPAGE = "http://openjade.sourceforge.net"
SECTION = "base"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=641ff1e4511f0a87044ad42f87cb1045"

PR = "r0"

DEPENDS = "opensp-native sgml-common-native"
RDEPENDS_${PN} = "sgml-common"

SRC_URI = "${SOURCEFORGE_MIRROR}/openjade/openjade-${PV}.tar.gz \
           file://makefile.patch"

SRC_URI[md5sum] = "7df692e3186109cc00db6825b777201e"
SRC_URI[sha256sum] = "1d2d7996cc94f9b87d0c51cf0e028070ac177c4123ecbfd7ac1cb8d0b7d322d1"

inherit autotools native

EXTRA_OECONF = "--enable-spincludedir=${STAGING_INCDIR}/OpenSP \
                --enable-splibdir=${STAGING_LIBDIR}"

CFLAGS =+ "-I${S}/include"

do_install() {
	# Refer to http://www.linuxfromscratch.org/blfs/view/stable/pst/openjade.html
	# for details.
	install -d ${D}${bindir}	
	install -m 0755 ${S}/jade/.libs/openjade ${D}${bindir}/openjade
	ln -sf openjade ${D}${bindir}/jade

	oe_libinstall -a -so -C style libostyle ${D}${libdir}
	oe_libinstall -a -so -C spgrove libospgrove ${D}${libdir}
	oe_libinstall -a -so -C grove libogrove ${D}${libdir}

	install -d ${D}${datadir}/sgml/openjade-${PV}
	install -m 644 dsssl/catalog ${D}${datadir}/sgml/openjade-${PV}
	install -m 644 dsssl/*.{dtd,dsl,sgm} ${D}${datadir}/sgml/openjade-${PV}

	# The catalog must live in the sysroot and it must be there for
	# install-catalog to do its thing.
	install -d ${datadir}/sgml/openjade-${PV}
	install -m 644 dsssl/catalog ${datadir}/sgml/openjade-${PV}/catalog
	install-catalog --add ${sysconfdir}/sgml/openjade-${PV}.cat \
		${datadir}/sgml/openjade-${PV}/catalog

	install-catalog --add ${sysconfdir}/sgml/sgml-docbook.cat \
		${sysconfdir}/sgml/openjade-${PV}.cat
}
