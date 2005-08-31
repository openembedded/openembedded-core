PR = "r0"
DESCRIPTION = "GNOME XML library"
SECTION = "libs"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
LICENSE = "MIT"
PACKAGES = "${PN}-dev ${PN}-utils ${PN} ${PN}-doc ${PN}-locale"

FILES_${PN}-dev += "${bindir}/xml-config"
FILES_${PN}-utils += "${bindir}"

SRC_URI = "http://xmlsoft.org/sources/old/libxml2-${PV}.tar.gz"

inherit autotools pkgconfig 

EXTRA_OECONF = "--without-python --without-debug --without-legacy --without-schemas --without-catalog --without-docbook --without-c14n"

headers = "DOCBparser.h HTMLparser.h HTMLtree.h SAX.h SAX2.h c14n.h catalog.h chvalid.h debugXML.h dict.h encoding.h entities.h globals.h hash.h list.h nanoftp.h nanohttp.h parser.h parserInternals.h pattern.h relaxng.h schemasInternals.h threads.h tree.h uri.h valid.h xinclude.h xlink.h xmlIO.h xmlautomata.h xmlerror.h xmlexports.h xmlmemory.h xmlreader.h xmlregexp.h xmlschemas.h xmlschemastypes.h xmlstring.h xmlunicode.h xmlversion.h xmlwriter.h xpath.h xpathInternals.h xpointer.h"

do_stage () {
	oe_libinstall -so libxml2 ${STAGING_LIBDIR}

	mkdir -p ${STAGING_INCDIR}/libxml2/libxml
	for i in ${headers}; do
		install -m 0644 include/libxml/$i ${STAGING_INCDIR}/libxml2/libxml/$i
	done

	cat xml2-config | sed -e "s,^prefix=.*,prefix=${STAGING_BINDIR}/..," \
		       	     -e "s,^exec_prefix=.*,exec_prefix=${STAGING_BINDIR}/..," \
			     -e "s,^includedir=.*,includedir=${STAGING_INCDIR}," \
			     -e "s,^libdir=.*,libdir=${STAGING_LIBDIR}," > ${STAGING_BINDIR}/xml2-config
	chmod a+rx ${STAGING_BINDIR}/xml2-config
	install -m 0644 libxml.m4 ${STAGING_DATADIR}/aclocal/
}

python populate_packages_prepend () {
	# autonamer would call this libxml2-2, but we don't want that
	if bb.data.getVar('DEBIAN_NAMES', d, 1):
		bb.data.setVar('PKG_libxml2', 'libxml2', d)
}
