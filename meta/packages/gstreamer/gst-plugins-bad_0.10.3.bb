require gst-plugins.inc
DEPENDS += "gst-plugins-base"

EXTRA_OECONF += "--disable-examples --disable-experimental --disable-sdl \
		--with-plugins=divx,libmms,musicbrainz,swfdec,xvid,wavpack"

