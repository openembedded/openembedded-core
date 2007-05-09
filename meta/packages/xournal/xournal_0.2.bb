HOMEPAGE = "http://www-math.mit.edu/~auroux/software/xournal/"
DESCRIPTION = "Xournal is an application for notetaking, sketching, keeping a journal using a stylus."
DEPENDS = "gtk+ libgnomecanvas zlib"
SECTION = "x11"
LICENSE = "GPL"
PR = "r2"

SRC_URI = "http://math.mit.edu/~auroux/software/xournal/xournal-0.2.tar.gz \
           file://ldflags.patch;patch=1 \
           file://no-printing.diff;patch=1"

inherit autotools pkgconfig
