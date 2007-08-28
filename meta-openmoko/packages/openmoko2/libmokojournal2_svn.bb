SECTION = "openmoko/libs"
DEPENDS = "eds-dbus"
PV = "0.1.0+svn${SVNREV}"
PR = "r2"

inherit openmoko2 lib_package

do_stage() {
        autotools_stage_all
}

