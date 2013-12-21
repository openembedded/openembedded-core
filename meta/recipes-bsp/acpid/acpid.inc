SUMMARY = "A daemon for delivering ACPI events"
HOMEPAGE = "http://sourceforge.net/projects/acpid/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=33140&atid=407341"
SECTION = "base"
LICENSE = "GPLv2+"

SRC_URI = "${SOURCEFORGE_MIRROR}/acpid/acpid-${PV}.tar.gz \
           file://init \
           file://set_socket_noblock.patch "

inherit update-rc.d

INITSCRIPT_NAME = "acpid"
INITSCRIPT_PARAMS = "defaults"

# Makefile ignores our CFLAGS, so override it.
#
EXTRA_OEMAKE = "CFLAGS='-W -Wall -Werror -Wundef -Wshadow ${CFLAGS} $(DEFS)'"

do_compile () {
	oe_runmake 'CC=${CC} -D_GNU_SOURCE' 'CROSS=${HOST_PREFIX}'
}

do_install () {
	install -d ${D}${bindir} 
	oe_runmake 'INSTPREFIX=${D}' install
	install -d ${D}${sysconfdir}/init.d
	cat ${WORKDIR}/init | sed -e's,/usr/sbin,${sbindir},g' > ${D}${sysconfdir}/init.d/acpid
	chmod 755 ${D}${sysconfdir}/init.d/acpid

	install -d ${D}${sysconfdir}/acpi
	install -d ${D}${sysconfdir}/acpi/events
}
