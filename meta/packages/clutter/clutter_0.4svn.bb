require clutter.inc

PV = "0.4.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/branches;module=clutter-0-4;proto=http \
	   file://enable_tests-0.4.patch;patch=1 "

S = "${WORKDIR}/clutter-0-4"

do_stage () {
        cp ${S}/clutter.pc ${S}/clutter-0.4.pc
        autotools_stage_all
}
