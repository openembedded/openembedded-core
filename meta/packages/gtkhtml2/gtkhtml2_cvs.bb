SECTION = "libs"
DEPENDS = "gtk+ glib-2.0 libxml2"
DESCRIPTION = "A GTK+ HTML rendering library."
LICENSE = "GPL"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
PV = "2.11.0+cvs${SRCDATE}"
PR = "r3"

SRC_URI = "cvs://anonymous@anoncvs.gnome.org/cvs/gnome;module=gtkhtml2 \
	http://svn.o-hand.com/repos/web/trunk/patches/at-import_box-pos.patch;patch=1;pnum=0 \
	http://svn.o-hand.com/repos/web/trunk/patches/css-stylesheet-user.patch;patch=1;pnum=0 \
	http://svn.o-hand.com/repos/web/trunk/patches/css-media.patch;patch=1;pnum=0 \
	http://svn.o-hand.com/repos/web/trunk/patches/add-end-element-signal.patch;patch=1;pnum=0 \
	http://svn.o-hand.com/repos/web/trunk/patches/add-dom-functions.patch;patch=1;pnum=0 \
	http://svn.o-hand.com/repos/web/trunk/patches/iain-mem-leak.patch;patch=1;pnum=0"

S = "${WORKDIR}/${PN}"

inherit pkgconfig autotools

EXTRA_OECONF = " --disable-accessibility"

do_stage() {
        oe_libinstall -so -C libgtkhtml libgtkhtml-2 ${STAGING_LIBDIR}
        install -d ${STAGING_INCDIR}/gtkhtml-2.0/libgtkhtml
	( for i in css document dom dom/core dom/events dom/html dom/traversal dom/views graphics layout layout/html util view; do install -d ${STAGING_INCDIR}/gtkhtml-2.0/libgtkhtml/$i; install -m 0644 ${S}/libgtkhtml/$i/*.h ${STAGING_INCDIR}/gtkhtml-2.0/libgtkhtml/$i; done )
	install -m 0644 ${S}/libgtkhtml/*.h ${STAGING_INCDIR}/gtkhtml-2.0/libgtkhtml
}

