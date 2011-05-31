DESCRIPTION = "System-V like init."
DESCRIPTION = "This package is required to boot in most configurations.  It provides the /sbin/init program.  This is the first process started on boot, and the last process terminated before the system halts."
HOMEPAGE = "http://savannah.nongnu.org/projects/sysvinit/"
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://COPYRIGHT;endline=15;md5=349c872e0066155e1818b786938876a4"
PR = "r3"

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

SRC_URI = "http://download.savannah.gnu.org/releases-noredirect/sysvinit/sysvinit-${PV}.tar.bz2 \
	   file://install.patch \
	   file://crypt-lib.patch \
           file://need \
           file://provide \
           file://inittab \
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
	install -m 0644 ${WORKDIR}/inittab ${D}${sysconfdir}/inittab
	if [ ! -z "${SERIAL_CONSOLE}" ]; then
		echo "S:2345:respawn:${base_sbindir}/getty ${SERIAL_CONSOLE}" >> ${D}${sysconfdir}/inittab
	fi
	if [ "${USE_VT}" = "1" ]; then
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
	mv ${D}${base_sbindir}/poweroff ${D}${base_sbindir}/poweroff.${PN}
	mv ${D}${bindir}/last ${D}${bindir}/last.${PN}
	mv ${D}${bindir}/mesg ${D}${bindir}/mesg.${PN}
	mv ${D}${bindir}/wall ${D}${bindir}/wall.${PN}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_sbindir}/halt halt halt.${PN} 200
	update-alternatives --install ${base_sbindir}/reboot reboot reboot.${PN} 200
	update-alternatives --install ${base_sbindir}/shutdown shutdown shutdown.${PN} 200
	update-alternatives --install ${base_sbindir}/poweroff poweroff poweroff.${PN} 200
	update-alternatives --install ${bindir}/last last last.${PN} 200
	update-alternatives --install ${bindir}/mesg mesg mesg.${PN} 200
	update-alternatives --install ${bindir}/wall wall wall.${PN} 200
}

pkg_prerm_${PN} () {
	update-alternatives --remove halt halt.${PN}
	update-alternatives --remove reboot reboot.${PN}
	update-alternatives --remove shutdown shutdown.${PN}
	update-alternatives --remove poweroff poweroff.${PN}
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
