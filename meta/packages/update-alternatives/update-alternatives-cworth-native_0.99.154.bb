require update-alternatives-cworth.inc
inherit native

PROVIDES += "virtual/update-alternatives-native"
RPROVIDES_${PN} += "update-alternatives-native"

do_stage () {
    install -d ${sbindir} \
               ${libdir}/opkg/alternatives

    install -m 0755 update-alternatives ${sbindir}/update-alternatives
}
