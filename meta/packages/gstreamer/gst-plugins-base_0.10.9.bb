require gst-plugins.inc
DEPENDS += "libx11 alsa-lib freetype gnome-vfs liboil libogg libvorbis tremor"
PROVIDES_${PN} += "gst-plugins"

EXTRA_OECONF += "--with-plugins=ximagesink,alsa,gnomevfs,ogg,vorbis \
		--disable-freetypetest --disable-pango --disable-theora"

do_stage() {
       autotools_stage_all
}

