require gst-plugins.inc
DEPENDS += "gst-plugins-base libmusicbrainz tremor"
PR = "r4"

EXTRA_OECONF += "--disable-examples --disable-experimental --disable-sdl \
		--with-plugins=musicbrainz,wavpack,ivorbis"

SRC_URI += " file://ivorbisdec.patch;patch=1;pnum=0"
