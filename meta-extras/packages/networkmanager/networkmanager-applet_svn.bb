DESCRIPTION = "GTK+ applet for NetworkManager" 
HOMEPAGE = "http://projects.gnome.org/NetworkManager/"
BUGTRACKER = "https://bugzilla.gnome.org/buglist.cgi?query_format=specific&order=relevance+desc&bug_status=__open__&product=NetworkManager&content="
LICENSE = "GPLv2+ & LGPLv2.1+"
DEPENDS = "networkmanager dbus-glib libglade gconf gnome-keyring"
#TODO DEPENDS libnotify
RDEPENDS = "networkmanager dbus-wait"
PR = "r7"

inherit gnome gtk-icon-cache

SRC_URI = "svn://svn.gnome.org/svn/network-manager-applet/;module=trunk;proto=http \
           file://applet-no-gnome.diff;patch=1;pnum=0 \
           file://applet-no-animation.patch;patch=1 \
           file://no_vpn.patch;patch=1 \
           file://nmutil-fix.patch;patch=1 \
           file://70NetworkManagerApplet.shbg"

PV = "0.0+svnr${SRCREV}"

S = "${WORKDIR}/trunk"

FILES_${PN} += "${datadir}/nm-applet/ \
                ${datadir}/gnome-vpn-properties/ \
                ${datadir}/gnome/autostart/"

do_install_append () {
	install -d ${D}${sysconfdir}/X11/Xsession.d/
	install -m 755 ${WORKDIR}/70NetworkManagerApplet.shbg ${D}${sysconfdir}/X11/Xsession.d/
}

#TODO: remove if libnotify in DEPENDS
EXTRA_OECONF += "--without-libnotify"
