CMAKE_MAJOR_VERSION="2.6"
inherit native
require cmake.inc

do_stage() {
    oe_runmake install
    autotools_stage_all
}

do_install() {
	:
}
