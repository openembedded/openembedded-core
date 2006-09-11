require gst-plugins.inc
DEPENDS += "gst-plugins-base libmusicbrainz"
PR = "r2"

EXTRA_OECONF += "--disable-examples --disable-experimental --disable-sdl \
		--with-plugins=musicbrainz,wavpack"

