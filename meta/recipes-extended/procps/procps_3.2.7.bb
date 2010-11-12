require procps.inc

PR = "r10"

inherit update-rc.d update-alternatives

INITSCRIPT_NAME = "procps.sh"
INITSCRIPT_PARAMS = "start 30 S ."

ALTERNATIVE_LINKS = "${bindir}/top ${bindir}/uptime ${bindir}/free ${bindir}/pkill \
                     ${base_bindir}/kill ${base_sbindir}/sysctl ${base_bindir}/ps"
ALTERNATIVE_PRIORITY = "90"

SRC_URI += "file://procmodule.patch;patch=1 \
            file://psmodule.patch;patch=1 \
	    file://linux-limits.patch;patch=1 \
	    file://sysctl.conf \
	    file://procps.init \
	    file://procps-3.2.8+gmake-3.82.patch \
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
}
