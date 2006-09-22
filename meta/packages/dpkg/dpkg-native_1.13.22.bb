require dpkg.inc
PR = "r1"
DEPENDS += "ncurses-native zlib-native virtual/update-alternatives-native"
SRC_URI += "file://noman.patch;patch=1"

inherit native

EXTRA_OECONF = "--without-static-progs \
		--without-dselect \
		--with-start-stop-daemon \
		--with-zlib \
		--with-bz2lib \
		--without-sgml-doc"
