SECTION = "base"
require e2fsprogs_${PV}.bb
inherit native

DEPENDS = "gettext-native"
FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/e2fsprogs-${PV}', '${FILE_DIRNAME}/e2fsprogs', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"
PACKAGES = ""

do_stage () {
        oe_runmake install
}
