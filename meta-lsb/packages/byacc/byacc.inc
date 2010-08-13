DESCRIPTION = "Berkeley Yacc"
SECTION = "devel"
LICENSE = "public domain"

SRC_URI = "ftp://invisible-island.net/byacc/byacc-${PV}.tgz"
EXTRA_OECONF += "--program-transform-name='s,^,b,'"

BBCLASSEXTEND = "native"

inherit autotools

do_configure() {
	oe_runconf
}
