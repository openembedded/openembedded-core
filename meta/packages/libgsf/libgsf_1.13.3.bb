LICENSE = "GPL"
SECTION = "libs"
PR = "r1"

DEPENDS= "libxml2 glib-2.0 zlib gtk-doc libbonobo gnome-vfs"
RDEPENDS = "gconf gnome-vfs"


PACKAGES =+ "${PN}-gnome ${PN}-gnome-dev "

FILES_${PN}-gnome = "${libdir}/libgsf-gnome-1.so.*"
FILES_${PN}-gnome-dev = "${libdir}/libgsf-gnome-1.* ${includedir}/libgsf-1/gsf-gnome"

inherit autotools pkgconfig gnome


do_stage() {
autotools_stage_all
}
