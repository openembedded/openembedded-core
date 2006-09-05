require dpkg.inc
SRC_URI += "file://noman.patch;patch=1"

inherit native
inherit autotools gettext

EXTRA_OECONF = "--without-static-progs \
		--without-dselect \
		--with-start-stop-daemon \
		--with-zlib \
		--with-bz2lib \
		--without-sgml-doc"
