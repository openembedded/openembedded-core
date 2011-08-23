require procps.inc

PR = "r2"

inherit update-rc.d update-alternatives

INITSCRIPT_NAME = "procps.sh"
INITSCRIPT_PARAMS = "start 30 S ."

ALTERNATIVE_LINKS = "${bindir}/top ${bindir}/uptime ${bindir}/free ${bindir}/pkill ${bindir}/pmap \
                     ${base_bindir}/kill ${base_sbindir}/sysctl ${base_bindir}/ps"
ALTERNATIVE_PRIORITY = "90"

SRC_URI += "file://procmodule.patch \
            file://psmodule.patch \
	    file://linux-limits.patch \
	    file://sysctl.conf \
	    file://procps.init \
	    file://procps-3.2.8+gmake-3.82.patch \
	    "

SRC_URI[md5sum] = "9532714b6846013ca9898984ba4cd7e0"
SRC_URI[sha256sum] = "11ed68d8a4433b91cd833deb714a3aa849c02aea738c42e6b4557982419c1535"

FILES = "${bindir}/top.${BPN} ${base_bindir}/ps.${BPN} ${bindir}/uptime.${BPN} ${base_bindir}/kill.${BPN} \
	 ${bindir}/free.${BPN} ${bindir}/w ${bindir}/watch ${bindir}/pgrep ${bindir}/pmap ${bindir}/pwdx \
	 ${bindir}/snice ${bindir}/vmstat ${bindir}/slabtop ${bindir}/pkill ${bindir}/skill ${bindir}/tload \
	 ${base_sbindir}/sysctl.${BPN}"

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
