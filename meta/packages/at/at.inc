DESCRIPTION = "Delayed job execution and batch processing"
SECTION = "base"
DEPENDS = "flex"
RCONFLICTS_${PN} = "atd"
RREPLACES_${PN} = "atd"

inherit autotools

export LIBS = "-L${STAGING_LIBDIR}"

do_install () {
    oe_runmake "IROOT=${D}" install
}
