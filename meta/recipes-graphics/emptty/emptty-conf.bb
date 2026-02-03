require emptty.inc

SUMMARY += " (Default config)"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    oe_runmake -C ${S}/src/${GO_IMPORT} DESTDIR=${D} install-config
}

FILES:${PN} = "${sysconfdir}/emptty/conf"
CONFFILES:${PN} = "${sysconfdir}/emptty/conf"
RPROVIDES:${PN} += "virtual-emptty-conf"
