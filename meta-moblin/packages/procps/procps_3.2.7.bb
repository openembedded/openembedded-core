require procps.inc

PR = "r8"

inherit update-rc.d

INITSCRIPT_NAME = "procps.sh"
INITSCRIPT_PARAMS = "start 30 S ."

SRC_URI += "file://procmodule.patch;patch=1 \
            file://psmodule.patch;patch=1 \
	    file://linux-limits.patch;patch=1 \
	    file://sysctl.conf \
	    file://procps.init \
	    "

FILES = "${bindir}/top.${PN} ${base_bindir}/ps.${PN} ${bindir}/uptime.${PN} ${base_bindir}/kill.${PN} \
	 ${bindir}/free.${PN} ${bindir}/w ${bindir}/watch ${bindir}/pgrep ${bindir}/pmap ${bindir}/pwdx \
	 ${bindir}/snice ${bindir}/vmstat ${bindir}/slabtop ${bindir}/pkill ${bindir}/skill ${bindir}/tload \
	 ${base_sbindir}/sysctl.${PN}"

CONFFILES_${PN} = "${sysconfdir}/sysctl.conf"

EXTRA_OEMAKE = "CFLAGS=-I${STAGING_INCDIR} \
                CPPFLAGS=-I${STAGING_INCDIR} \
                LDFLAGS="${LDFLAGS}" \
                CURSES=-lncurses \
                install='install -D' \
                ldconfig=echo"

do_install_append () {
	install -d ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/sysctl.conf ${D}${sysconfdir}/sysctl.conf
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/procps.init ${D}${sysconfdir}/init.d/procps.sh

	mv ${D}${bindir}/uptime ${D}${bindir}/uptime.${PN}
	mv ${D}${bindir}/top ${D}${bindir}/top.${PN}
	mv ${D}${base_bindir}/kill ${D}${base_bindir}/kill.${PN}
	mv ${D}${base_bindir}/ps ${D}${base_bindir}/ps.${PN}
	mv ${D}${bindir}/free ${D}${bindir}/free.${PN}
	mv ${D}${base_sbindir}/sysctl ${D}${base_sbindir}/sysctl.${PN}
	mv ${D}${bindir}/pkill ${D}${bindir}/pkill.${PN}
}

pkg_postinst() {
	update-alternatives --install ${bindir}/top top top.${PN} 90
	update-alternatives --install ${bindir}/uptime uptime uptime.${PN} 90
	update-alternatives --install ${base_bindir}/ps ps ps.${PN} 90
	update-alternatives --install ${base_bindir}/kill kill kill.${PN} 90
	update-alternatives --install ${bindir}/free free free.${PN} 90
	update-alternatives --install ${base_sbindir}/sysctl sysctl sysctl.${PN} 90
	update-alternatives --install ${bindir}/pkill pkill pkill.${PN} 90
}

pkg_postrm() {
	update-alternatives --remove top top.${PN}
	update-alternatives --remove ps ps.${PN}
	update-alternatives --remove uptime uptime.${PN}
	update-alternatives --remove kill kill.${PN}
	update-alternatives --remove free free.${PN}
	update-alternatives --remove sysctl sysctl.${PN}
	update-alternatives --remove pkill pkill.${PN}
}
