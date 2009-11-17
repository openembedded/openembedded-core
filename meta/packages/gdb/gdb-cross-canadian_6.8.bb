require gdb-cross.inc

DEPENDS = "ncurses-nativesdk expat-nativesdk"

inherit cross-canadian

do_install () {
	autotools_do_install
}

do_stage () {
	autotools_stage_all
}
