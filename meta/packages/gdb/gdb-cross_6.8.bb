require gdb-cross.inc

inherit cross

do_install () {
	autotools_do_install
}

do_stage () {
	autotools_stage_all
}

SRC_URI += "file://sim-install-6.6.patch;patch=1"

PR = "r2"
