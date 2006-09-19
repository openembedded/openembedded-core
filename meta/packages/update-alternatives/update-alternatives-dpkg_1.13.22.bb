require update-alternatives-dpkg.inc

RPROVIDES_${PN} = "update-alternatives"
RDEPENDS_${PN} = "perl dpkg"

do_install () {
    install -d ${D}${sbindir} \
               ${D}${localstatedir}/dpkg/alternatives \
               ${D}${sysconfdir}/alternatives

    install -m 0755 scripts/update-alternatives ${D}${sbindir}/update-alternatives
}
