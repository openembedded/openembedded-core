DESCRIPTION = "ACPI data gathering library."
SECTION = "base"
HOMEPAGE = "http://www.ngolde.de/libacpi.html"
LICENSE = "MIT"

SRC_URI = "http://www.ngolde.de/download/libacpi-${PV}.tar.gz \
	   file://makefile-fix.patch;patch=1 "

FILES_${PN} = "${libdir}"
FILES_libacpi-dev = "${includedir}"

do_stage() {
	install -m 0644 libacpi.h ${STAGING_INCDIR}
	oe_libinstall -so libacpi ${STAGING_LIBDIR}
}

do_install() {
	oe_runmake install DESTDIR=${D} PREFIX=${exec_prefix}
}
