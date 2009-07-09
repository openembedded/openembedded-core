SECTION = "libs"
DEPENDS = "gtk+ glib-2.0 libxml2"
DESCRIPTION = "A GTK+ HTML rendering library."
LICENSE = "GPL"
PV = "2.11.0+svnr${SRCREV}"
PR = "r1"

SRC_URI = "svn://anonymous@svn.gnome.org/svn/gtkhtml2/;module=trunk \
	http://svn.o-hand.com/repos/web/trunk/patches/at-import_box-pos.patch;patch=1;pnum=0;maxrev=1157 \
	http://svn.o-hand.com/repos/web/trunk/patches/css-stylesheet-user.patch;patch=1;pnum=0;maxrev=1157 \
	http://svn.o-hand.com/repos/web/trunk/patches/css-media.patch;patch=1;pnum=0;maxrev=1157 \
	http://svn.o-hand.com/repos/web/trunk/patches/add-end-element-signal.patch;patch=1;pnum=0;maxrev=1157 \
	http://svn.o-hand.com/repos/web/trunk/patches/add-dom-functions.patch;patch=1;pnum=0;maxrev=1157 \
	http://svn.o-hand.com/repos/web/trunk/patches/iain-mem-leak.patch;patch=1;pnum=0;maxrev=1157"

S = "${WORKDIR}/trunk"

inherit pkgconfig autotools_stage

EXTRA_OECONF = " --disable-accessibility"
