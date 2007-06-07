LICENSE = "LGPL"
PR = "r2"

inherit gnome

EXTRA_OECONF = "--disable-gtk-doc --disable-python"

do_stage() {
autotools_stage_all
}
