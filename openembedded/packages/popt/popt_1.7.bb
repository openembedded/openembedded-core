SECTION = "libs"
DESCRIPTION = "The popt library exists essentially \
for parsing command line options."
LICENSE = "MIT"
PR = "r1"

SRC_URI = "ftp://ftp.rpm.org/pub/rpm/dist/rpm-4.1.x/popt-${PV}.tar.gz \
	   file://m4.patch;patch=1 \
	   file://intl.patch;patch=1"

inherit autotools 

do_stage () {
	oe_libinstall -a -so libpopt ${STAGING_LIBDIR}
	install -m 0644 popt.h ${STAGING_INCDIR}/
}
