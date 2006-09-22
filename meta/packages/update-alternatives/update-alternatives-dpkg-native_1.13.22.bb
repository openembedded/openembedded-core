require update-alternatives-dpkg.inc
inherit native

PROVIDES += "virtual/update-alternatives-native"
DEPENDS += "perl-native dpkg-native"
DEFAULT_PREFERENCE = "-1"

do_stage () {
    install -d ${sbindir} \
               ${localstatedir}/dpkg/alternatives \
               ${sysconfdir}/alternatives

    install -m 0755 scripts/update-alternatives ${sbindir}/update-alternatives
}
