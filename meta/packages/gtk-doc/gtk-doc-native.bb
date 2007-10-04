SECTION = "x11/base"
inherit native
require gtk-doc.inc
PR="r1"

SRC_URI = "file://gtk-doc.m4"
    
do_stage() {
	install -d ${STAGING_DATADIR}/aclocal
	install -m 0644 ${WORKDIR}/gtk-doc.m4 ${STAGING_DATADIR}/aclocal/
}
