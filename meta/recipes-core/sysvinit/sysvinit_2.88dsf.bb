DESCRIPTION = "System-V like init."
DESCRIPTION = "This package is required to boot in most configurations.  It provides the /sbin/init program.  This is the first process started on boot, and the last process terminated before the system halts."
HOMEPAGE = "http://savannah.nongnu.org/projects/sysvinit/"
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://COPYRIGHT;endline=15;md5=349c872e0066155e1818b786938876a4"
PR = "r5"

RDEPENDS_${PN} = "${PN}-inittab"

SRC_URI = "http://download.savannah.gnu.org/releases-noredirect/sysvinit/sysvinit-${PV}.tar.bz2 \
	   file://install.patch \
	   file://crypt-lib.patch \
           file://need \
           file://provide \
           file://rcS-default \
           file://rc \
           file://rcS \
	   file://bootlogd.init"

SRC_URI[md5sum] = "6eda8a97b86e0a6f59dabbf25202aa6f"
SRC_URI[sha256sum] = "60bbc8c1e1792056e23761d22960b30bb13eccc2cabff8c7310a01f4d5df1519"

S = "${WORKDIR}/sysvinit-${PV}"
B = "${S}/src"

inherit update-alternatives

ALTERNATIVE_NAME = "init"
ALTERNATIVE_LINK = "${base_sbindir}/init"
ALTERNATIVE_PATH = "${base_sbindir}/init.sysvinit"
ALTERNATIVE_PRIORITY = "50"

PACKAGES =+ "sysvinit-pidof sysvinit-sulogin"
FILES_${PN} += "${base_sbindir}/* ${base_bindir}/*"
FILES_sysvinit-pidof = "${base_bindir}/pidof.sysvinit ${base_sbindir}/killall5"
FILES_sysvinit-sulogin = "${base_sbindir}/sulogin"

RDEPENDS_${PN} += "sysvinit-pidof"

CFLAGS_prepend = "-D_GNU_SOURCE "
export LCRYPT = "-lcrypt"
EXTRA_OEMAKE += "'base_bindir=${base_bindir}' \
		 'base_sbindir=${base_sbindir}' \
		 'bindir=${bindir}' \
		 'sbindir=${sbindir}' \
		 'sysconfdir=${sysconfdir}' \
		 'includedir=${includedir}' \
		 'mandir=${mandir}'"

do_install () {
	oe_runmake 'ROOT=${D}' install
	install -d ${D}${sysconfdir} \
		   ${D}${sysconfdir}/default \
		   ${D}${sysconfdir}/init.d
	install -m 0644    ${WORKDIR}/rcS-default	${D}${sysconfdir}/default/rcS
	install -m 0755    ${WORKDIR}/rc		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/rcS		${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/bootlogd.init     ${D}${sysconfdir}/init.d/bootlogd
	ln -sf bootlogd ${D}${sysconfdir}/init.d/stop-bootlogd
	install -d ${D}${sysconfdir}/rcS.d
	ln -sf ../init.d/bootlogd ${D}${sysconfdir}/rcS.d/S07bootlogd
	for level in 2 3 4 5; do
		install -d ${D}${sysconfdir}/rc$level.d
		ln -s ../init.d/stop-bootlogd ${D}${sysconfdir}/rc$level.d/S99stop-bootlogd
	done
	mv                 ${D}${base_sbindir}/init               ${D}${base_sbindir}/init.${BPN}
	mv ${D}${base_bindir}/pidof ${D}${base_bindir}/pidof.${BPN}
	mv ${D}${base_sbindir}/halt ${D}${base_sbindir}/halt.${BPN}
	mv ${D}${base_sbindir}/reboot ${D}${base_sbindir}/reboot.${BPN}
	mv ${D}${base_sbindir}/shutdown ${D}${base_sbindir}/shutdown.${BPN}
	mv ${D}${base_sbindir}/poweroff ${D}${base_sbindir}/poweroff.${BPN}
	mv ${D}${bindir}/last ${D}${bindir}/last.${BPN}
	mv ${D}${bindir}/mesg ${D}${bindir}/mesg.${BPN}
	mv ${D}${bindir}/wall ${D}${bindir}/wall.${BPN}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_sbindir}/halt halt halt.${BPN} 200
	update-alternatives --install ${base_sbindir}/reboot reboot reboot.${BPN} 200
	update-alternatives --install ${base_sbindir}/shutdown shutdown shutdown.${BPN} 200
	update-alternatives --install ${base_sbindir}/poweroff poweroff poweroff.${BPN} 200
	update-alternatives --install ${bindir}/last last last.${BPN} 200
	update-alternatives --install ${bindir}/mesg mesg mesg.${BPN} 200
	update-alternatives --install ${bindir}/wall wall wall.${BPN} 200
}

pkg_prerm_${PN} () {
	update-alternatives --remove halt halt.${BPN}
	update-alternatives --remove reboot reboot.${BPN}
	update-alternatives --remove shutdown shutdown.${BPN}
	update-alternatives --remove poweroff poweroff.${BPN}
	update-alternatives --remove last last.${BPN}
	update-alternatives --remove mesg mesg.${BPN}
	update-alternatives --remove wall wall.${BPN}
}

pkg_postinst_sysvinit-pidof () {
	update-alternatives --install ${base_bindir}/pidof pidof pidof.${BPN} 200
}

pkg_prerm_sysvinit-pidof () {
	update-alternatives --remove pidof pidof.${BPN}
}
