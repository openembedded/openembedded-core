require dpkg.inc
DEPENDS += "ncurses zlib bzip2"
RDEPENDS_${PN} = "${VIRTUAL-RUNTIME_update-alternatives}"
SRC_URI += "file://noman.patch;patch=1"

EXTRA_OECONF = "--without-static-progs \
		--without-dselect \
		--with-start-stop-daemon \
		--with-zlib \
		--with-bz2lib \
		--without-sgml-doc"
