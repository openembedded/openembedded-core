LICENSE = "LGPL"
DEPENDS = "gtk+ intltool-native"

PR="r1"

inherit gnome

EXTRA_OECONF = "--disable-gtk-doc --disable-python"

do_stage() {
autotools_stage_all
}
