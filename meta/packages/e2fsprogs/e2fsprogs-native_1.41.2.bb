require e2fsprogs_${PV}.bb
inherit native

DEPENDS = "gettext-native"

do_stage () {
        oe_runmake install
}
