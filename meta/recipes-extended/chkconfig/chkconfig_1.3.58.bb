SUMMARY = "A system tool for maintaining the /etc/rc*.d hierarchy"

DESCRIPTION = "Chkconfig is a basic system utility.  It updates and queries runlevel \
information for system services.  Chkconfig manipulates the numerous \
symbolic links in /etc/rc.d, to relieve system administrators of some \
of the drudgery of manually editing the symbolic links."

HOMEPAGE = "http://fedorahosted.org/releases/c/h/chkconfig"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5574c6965ae5f583e55880e397fbb018"

DEPENDS = "libnewt popt"

PR = "r6"

SRC_URI = "http://fedorahosted.org/releases/c/h/chkconfig/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "c2039ca67f2749fe0c06ef7c6f8ee246"
SRC_URI[sha256sum] = "18b497d25b2cada955c72810e45fcad8280d105f17cf45e2970f18271211de68"

inherit gettext

# Makefile uses RPM_OPT_FLAGS to construct CFLAGS
#
EXTRA_OEMAKE = "\
    'RPM_OPT_FLAGS=${CFLAGS}' \
    'LDFLAGS=${LDFLAGS}' \
    'BINDIR=${base_sbindir}' \
    'SBINDIR=${sbindir}' \
    'MANDIR=${mandir}' \
"

do_unpack[postfuncs] += "obey_variables"
do_unpack[vardeps] += "obey_variables"
obey_variables () {
	sed -i -e 's,/etc,${sysconfdir},; s,/lib/systemd,${base_libdir}/systemd,' leveldb.h
}

do_install() {
	oe_runmake 'DESTDIR=${D}' 'INSTALLNLSDIR=${D}${datadir}/locale' install
	mkdir -p ${D}${sysconfdir}/chkconfig.d
	rm -f ${D}${sbindir}/update-alternatives
}

do_install_append_linuxstdbase() {
	install -d ${D}${libdir}/lsb/
	ln -sf ${base_sbindir}/chkconfig ${D}/${libdir}/lsb/install_initd
	ln -sf ${base_sbindir}/chkconfig ${D}/${libdir}/lsb/remove_initd
}

FILES_${PN}_append_linuxstdbase += "${libdir}/lsb"
