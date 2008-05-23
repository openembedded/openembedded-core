SECTION = "openmoko/libs"
DEPENDS = "gtk+"
PV = "0.1.0+svnr${SRCREV}"
PR = "r2"

inherit openmoko2

do_configure_prepend() {
        touch gtk-doc.make
}

do_stage() {
        autotools_stage_all
}

