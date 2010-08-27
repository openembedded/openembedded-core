DESCRIPTION = "ACPI data gathering library."
SECTION = "base"
HOMEPAGE = "http://www.ngolde.de/libacpi.html"
LICENSE = "MIT"
PR = "r1"

SRC_URI = "http://www.ngolde.de/download/libacpi-${PV}.tar.gz \
	   file://makefile-fix.patch;patch=1 "

PACKAGES += "${PN}-bin"

FILES_${PN} = "${libdir}/libacpi.so.*"
FILES_${PN}-bin = "${bindir}"

COMPATIBLE_HOST = '(x86_64|i.86.*)-(linux|freebsd.*)'

do_install() {
	oe_runmake install DESTDIR=${D} PREFIX=${exec_prefix}
}
