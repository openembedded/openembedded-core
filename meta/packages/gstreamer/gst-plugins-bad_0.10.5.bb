require gst-plugins.inc
DEPENDS += "gst-plugins-base libmusicbrainz tremor"
PR = "r5"

EXTRA_OECONF += "--disable-examples --disable-experimental --disable-sdl --disable-cdaudio \
		--with-plugins=musicbrainz,wavpack,ivorbis"

SRC_URI += " file://ivorbisdec.patch;patch=1;pnum=0"

ARM_INSTRUCTION_SET = "arm"
