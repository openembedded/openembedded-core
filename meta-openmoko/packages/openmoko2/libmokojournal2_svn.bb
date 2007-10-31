SECTION = "openmoko/libs"
DEPENDS = "eds-dbus"
PV = "0.1.0+svnr${SRCREV}"
PR = "r2"

inherit openmoko2 lib_package

do_configure_prepend() {
        touch gtk-doc.make
}

do_stage() {
        autotools_stage_all
}

