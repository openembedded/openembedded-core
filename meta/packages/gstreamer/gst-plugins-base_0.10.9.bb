require gst-plugins.inc
DEPENDS += "libx11 alsa-lib freetype gnome-vfs liboil libogg libvorbis tremor"
RDEPENDS += "gnome-vfs-plugin-file gnome-vfs-plugin-http gnome-vfs-plugin-ftp \
             gnome-vfs-plugin-sftp"
PROVIDES_${PN} += "gst-plugins"
PR = "r3"

EXTRA_OECONF += "--disable-freetypetest --disable-pango --disable-theora"

do_stage() {
       autotools_stage_all
}

