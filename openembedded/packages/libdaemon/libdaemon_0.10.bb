DESCRIPTION = "libdaemon is a lightweight C library which eases the writing of UNIX daemons."
SECTION = "libs"
AUTHOR = "Lennart Poettering <lennart@poettering.net>"
HOMEPAGE = "http://0pointer.de/lennart/projects/libdaemon/"
MAINTAINER = "Philipp Zabel <philipp.zabel@gmail.com>"
LICENSE = "LGPL"

SRC_URI = "http://0pointer.de/lennart/projects/libdaemon/libdaemon-${PV}.tar.gz"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-lynx --disable-doxygen"

do_stage () {
	oe_libinstall -a -so -C libdaemon libdaemon ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/libdaemon
	for i in dlog.h dexec.h dfork.h dsignal.h dnonblock.h dpid.h; do
		install -m 0644 ${S}/libdaemon/$i ${STAGING_INCDIR}/libdaemon/
	done
}
