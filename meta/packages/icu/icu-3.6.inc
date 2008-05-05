
LICENSE = "ICU"

SRC_URI = "ftp://ftp.software.ibm.com/software/globalization/icu/3.6/icu4c-3_6-src.tgz"

S = "${WORKDIR}/icu/source"

PARALLEL_MAKE = ""

inherit autotools pkgconfig binconfig

do_configure() {
	libtoolize --force
	gnu-configize --force
	oe_runconf
}	


