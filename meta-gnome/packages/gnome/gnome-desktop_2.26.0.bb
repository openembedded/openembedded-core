require gnome-desktop.inc

inherit gnome pkgconfig

DEPENDS += "gnome-doc-utils gnome-vfs libxrandr"

SRC_URI += " \
            file://no-desktop-docs.patch;patch=1;pnum=0"
