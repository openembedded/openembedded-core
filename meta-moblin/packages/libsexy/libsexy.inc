DESCRIPTION = "Libsexy is a collection of GTK+ widgets that extend the functionality of such standard widgets as GtkEntry and GtkLabel"
HOMEPAGE = "http://wiki.chipx86.com/wiki/Libsexy"
AUTHOR = "Christian Hammond <chipx86@chipx86.com>"
SECTION = "x11/libs"

DEPENDS = "gtk+ enchant libxml2"

SRC_URI = "http://releases.chipx86.com/libsexy/${PN}/${PN}-${PV}.tar.gz \
           file://libsexy-pkgconfig-fixes.patch;patch=1"

inherit autotools pkgconfig


