DESCRIPTION = "A daemon for delivering ACPI events."
HOMEPAGE = "http://sourceforge.net/projects/acpid/"
BUGTRACKER = "http://sourceforge.net/tracker/?group_id=33140&atid=407341"

LICENSE="GPLv2+"

SECTION = "base"
SRC_URI = "${SOURCEFORGE_MIRROR}/acpid/acpid-${PV}.tar.gz \
           file://init"

inherit update-rc.d

INITSCRIPT_NAME = "acpid"
INITSCRIPT_PARAMS = "defaults"

EXTRA_OEMAKE = ""

do_compile () {
	oe_runmake 'CC=${CC} -D_GNU_SOURCE' 'CROSS=${HOST_PREFIX}'
}

do_install () {
	install -d ${D}${bindir} 
	oe_runmake 'INSTPREFIX=${D}' install
	install -d ${D}${sysconfdir}/init.d
	cat ${WORKDIR}/init | sed -e's,/usr/sbin,${sbindir},g' > ${D}${sysconfdir}/init.d/acpid
	chmod 755 ${D}${sysconfdir}/init.d/acpid
}
