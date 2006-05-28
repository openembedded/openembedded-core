DESCRIPTION = "System-V like init."
SECTION = "base"
LICENSE = "GPL"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"
HOMEPAGE = "http://freshmeat.net/projects/sysvinit/"
PR = "r24"

# USE_VT and SERIAL_CONSOLE are generally defined by the MACHINE .conf.
# Set PACKAGE_ARCH appropriately.
PACKAGE_ARCH_${PN}-inittab = "${MACHINE_ARCH}"

RDEPENDS_${PN} = "${PN}-inittab"

PACKAGES =+ "bootlogd ${PN}-inittab"
FILES_bootlogd = "/etc/init.d/bootlogd /etc/init.d/stop-bootlogd /etc/rc?.d/S*bootlogd /sbin/bootlogd"
FILES_${PN}-inittab = "${sysconfdir}/inittab"
CONFFILES_${PN}-inittab = "${sysconfdir}/inittab"

USE_VT ?= "1"
SYSVINIT_ENABLED_GETTYS ?= "1"

SRC_URI = "ftp://ftp.cistron.nl/pub/people/miquels/sysvinit/sysvinit-${PV}.tar.gz \
	   file://install.patch;patch=1 \
           file://need \
           file://provide \
           file://inittab \
           file://rcS-default \
           file://rc \
           file://rcS \
	   file://bootlogd.init"

S = "${WORKDIR}/sysvinit-${PV}"
B = "${S}/src"

inherit update-alternatives

ALTERNATIVE_NAME = "init"
ALTERNATIVE_LINK = "${base_sbindir}/init"
ALTERNATIVE_PATH = "${base_sbindir}/init.sysvinit"
ALTERNATIVE_PRIORITY = "50"

PACKAGES =+ "sysvinit-pidof sysvinit-sulogin"
FILES_${PN} += "${base_sbindir} ${base_bindir}"
FILES_sysvinit-pidof = "${base_bindir}/pidof.sysvinit"
FILES_sysvinit-sulogin = "${base_sbindir}/sulogin"

CFLAGS_prepend = "-D_GNU_SOURCE "
export LCRYPT = "-lcrypt"
EXTRA_OEMAKE += "'INSTALL=install' \
		 'bindir=${base_bindir}' \
		 'sbindir=${base_sbindir}' \
		 'usrbindir=${bindir}' \
		 'usrsbindir=${sbindir}' \
		 'includedir=${includedir}' \
		 'mandir=${mandir}'"

do_install () {
	oe_runmake 'ROOT=${D}' install
	install -d ${D}${sysconfdir} \
		   ${D}${sysconfdir}/default \
		   ${D}${sysconfdir}/init.d
	install -m 0644 ${WORKDIR}/inittab ${D}${sysconfdir}/inittab
	if [ ! -z "${SERIAL_CONSOLE}" ]; then
		echo "S:2345:respawn:${base_sbindir}/getty ${SERIAL_CONSOLE}" >> ${D}${sysconfdir}/inittab
	fi
	if [ "${USE_VT}" == "1" ]; then
		cat <<EOF >>${D}${sysconfdir}/inittab
# ${base_sbindir}/getty invocations for the runlevels.
#
# The "id" field MUST be the same as the last
# characters of the device (after "tty").
#
# Format:
#  <id>:<runlevels>:<action>:<process>
#

EOF

		for n in ${SYSVINIT_ENABLED_GETTYS}
		do
			echo "$n:2345:respawn:${base_sbindir}/getty 38400 tty$n" >> ${D}${sysconfdir}/inittab
		done
		echo "" >> ${D}${sysconfdir}/inittab
	fi
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
	mv                 ${D}${base_sbindir}/init               ${D}${base_sbindir}/init.${PN}
	mv ${D}${base_bindir}/pidof ${D}${base_bindir}/pidof.${PN}
	mv ${D}${base_sbindir}/halt ${D}${base_sbindir}/halt.${PN}
	mv ${D}${base_sbindir}/reboot ${D}${base_sbindir}/reboot.${PN}
	mv ${D}${base_sbindir}/shutdown ${D}${base_sbindir}/shutdown.${PN}
	mv ${D}${bindir}/last ${D}${bindir}/last.${PN}
	mv ${D}${bindir}/mesg ${D}${bindir}/mesg.${PN}
	mv ${D}${bindir}/wall ${D}${bindir}/wall.${PN}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_sbindir}/halt halt halt.${PN} 200
	update-alternatives --install ${base_sbindir}/reboot reboot reboot.${PN} 200
	update-alternatives --install ${base_sbindir}/shutdown shutdown shutdown.${PN} 200
	update-alternatives --install ${bindir}/last last last.${PN} 200
	update-alternatives --install ${bindir}/mesg mesg mesg.${PN} 200
	update-alternatives --install ${bindir}/wall wall wall.${PN} 200
}

pkg_prerm_${PN} () {
	update-alternatives --remove halt halt.${PN}
	update-alternatives --remove reboot reboot.${PN}
	update-alternatives --remove shutdown shutdown.${PN}
	update-alternatives --remove last last.${PN}
	update-alternatives --remove mesg mesg.${PN}
	update-alternatives --remove wall wall.${PN}
}

pkg_postinst_sysvinit-pidof () {
	update-alternatives --install ${base_bindir}/pidof pidof pidof.${PN} 200
}

pkg_prerm_sysvinit-pidof () {
	update-alternatives --remove pidof pidof.${PN}
}
