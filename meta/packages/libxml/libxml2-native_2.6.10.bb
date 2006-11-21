DESCRIPTION = "GNOME XML library"

SRC_URI = "ftp://xmlsoft.org/libxml2/old/libxml2-${PV}.tar.gz"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libxml2-${PV}"
S = "${WORKDIR}/libxml2-${PV}"

inherit autotools native pkgconfig 

EXTRA_OECONF = "--without-python --without-debug --without-legacy --without-schemas --without-catalog --without-docbook --without-c14n"

headers = "DOCBparser.h HTMLparser.h HTMLtree.h SAX.h SAX2.h c14n.h catalog.h chvalid.h debugXML.h dict.h encoding.h entities.h globals.h hash.h list.h nanoftp.h nanohttp.h parser.h parserInternals.h pattern.h relaxng.h schemasInternals.h threads.h tree.h uri.h valid.h xinclude.h xlink.h xmlIO.h xmlautomata.h xmlerror.h xmlexports.h xmlmemory.h xmlreader.h xmlregexp.h xmlschemas.h xmlschemastypes.h xmlstring.h xmlunicode.h xmlversion.h xmlwriter.h xpath.h xpathInternals.h xpointer.h"

do_stage () {
	oe_runmake install
}
