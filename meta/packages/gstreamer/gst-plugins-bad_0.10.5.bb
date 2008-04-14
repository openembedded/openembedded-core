require gst-plugins.inc
DEPENDS += "gst-plugins-base libmusicbrainz tremor"
PR = "r6"

EXTRA_OECONF += "--disable-examples --disable-experimental --disable-sdl --disable-cdaudio \
		--with-plugins=musicbrainz,wavpack,ivorbis"

SRC_URI += " file://ivorbisdec.patch;patch=1;pnum=0"

ARM_INSTRUCTION_SET = "arm"

do_configure_prepend() {
	# This m4 file contains nastiness which conflicts with libtool 2.2.2
	rm ${S}/m4/lib-link.m4 || true
}
