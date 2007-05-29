DESCRIPTION = "Empathy: a Telepathy based IM client"
HOMEPAGE = "http://blogs.gnome.org/view/xclaesse/2007/04/26/0"
LICENSE = "GPL"
DEPENDS = "telepathy-mission-control libtelepathy gtk+ gconf libglade gnome-vfs"
RDEPENDS = "telepathy-mission-control"
RRECOMMENDS = "telepathy-gabble"

PR="r1"

inherit gnome gtk-icon-cache

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/empathy/0.5/empathy-${PV}.tar.bz2 \
           file://no-gnome.diff;patch=1;pnum=0"

FILES_${PN} += "${datadir}/mission-control/profiles/*.profile \
        ${datadir}/dbus-1/services/*.service \
        ${datadir}/telepathy/managers/*.chandler"
