require update-alternatives-cworth.inc

RPROVIDES_${PN} = "update-alternatives"

do_install () {
    install -d ${D}${sbindir} \
               ${D}${sysconfdir}/alternatives \
               ${D}${libdir}/ipkg/alternatives

    install -m 0755 update-alternatives ${D}${sbindir}/update-alternatives
}
