DESCRIPTION = "Evince is a document viewer for document formats like PDF, PS, DjVu."
LICENSE = "GPL"
SECTION = "x11/office"
DEPENDS = "gnome-doc-utils poppler libxml2 gtk+ gnome-vfs gconf libglade gnome-keyring"

inherit gnome pkgconfig gtk-icon-cache

SRC_URI += "file://no-icon-theme.diff;patch=1;pnum=0"

EXTRA_OECONF = "--without-libgnome --disable-thumbnailer"
