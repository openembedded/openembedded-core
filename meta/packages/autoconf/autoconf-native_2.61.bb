require autoconf_${PV}.bb

DEPENDS = "m4-native gnu-config-native"
RDEPENDS_${PN} = "m4-native gnu-config-native"

S = "${WORKDIR}/autoconf-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/autoconf-${PV}"

inherit native

#
# without it build break:
# | make[1]: *** No rule to make target `../bin/autom4te', needed by `autoconf.in'.  Stop.
#
PARALLEL_MAKE = ""
