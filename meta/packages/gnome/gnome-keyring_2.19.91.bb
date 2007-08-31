LICENSE = "GPL"
SECTION = "x11/gnome"

inherit autotools gnome pkgconfig

DEPENDS = "gtk+ libgcrypt"

EXTRA_OECONF = "--disable-gtk-doc"

do_stage() {
        autotools_stage_all
}
