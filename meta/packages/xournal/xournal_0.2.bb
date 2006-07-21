HOMEPAGE = "http://www-math.mit.edu/~auroux/software/xournal/"
DESCRIPTION = "Xournal is an application for notetaking, sketching, keeping a journal using a stylus."
MAINTAINER = "Koen Kooi <koen@dominion.kabel.utwente.nl>"
DEPENDS = "gtk+ libgnomecanvas"
SECTION = "x11"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://math.mit.edu/~auroux/software/xournal/xournal.tar.gz \
        file://no-printing.diff;patch=1"

inherit autotools pkgconfig
