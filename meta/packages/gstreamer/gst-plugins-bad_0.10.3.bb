require gst-plugins.inc
DEPENDS += "gst-plugins-base libmusicbrainz tremor"
PR = "r3"

EXTRA_OECONF += "--disable-examples --disable-experimental --disable-sdl \
		--with-plugins=musicbrainz,wavpack,ivorbis"

