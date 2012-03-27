require procps.inc

PR = "r7"

inherit update-alternatives

ALTERNATIVE_LINKS = "${bindir}/top ${bindir}/uptime ${bindir}/free ${bindir}/pkill ${bindir}/pmap \
                     ${base_bindir}/kill ${base_sbindir}/sysctl ${base_bindir}/ps \
                     ${bindir}/pgrep ${bindir}/pwdx ${bindir}/watch"
ALTERNATIVE_PRIORITY = "110"

SRC_URI += "file://procmodule.patch \
            file://psmodule.patch \
            file://linux-limits.patch \
            file://sysctl.conf \
            file://procps-3.2.8+gmake-3.82.patch \
            file://gnu-kbsd-version.patch \
           "

SRC_URI[md5sum] = "9532714b6846013ca9898984ba4cd7e0"
SRC_URI[sha256sum] = "11ed68d8a4433b91cd833deb714a3aa849c02aea738c42e6b4557982419c1535"

EXTRA_OEMAKE = "CFLAGS=-I${STAGING_INCDIR} \
                CPPFLAGS=-I${STAGING_INCDIR} \
                LDFLAGS="${LDFLAGS}" \
                CURSES=-lncurses \
                install='install -D' \
                ldconfig=echo"

do_install_append () {
	install -d ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/sysctl.conf ${D}${sysconfdir}/sysctl.conf
}

CONFFILES_${PN} = "${sysconfdir}/sysctl.conf"
