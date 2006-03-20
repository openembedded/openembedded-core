LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "gtk+ libgnomecanvas"
MAINTAINER = "Ross Burton <ross@openedhand.com>
DESCRIPTION = "An app for notetaking, sketching, or keeping a journal"
PR = "r1"

SRC_URI = "http://math.mit.edu/~auroux/software/xournal/xournal.tar.gz \
        file://no-printing.diff;patch=1"

inherit autotools pkgconfig
