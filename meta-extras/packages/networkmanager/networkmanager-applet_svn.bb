DESCRIPTION = "GTK+ applet for NetworkManager" 
LICENSE = "GPL"
DEPENDS = "networkmanager dbus-glib libglade gconf gnome-keyring"
#TODO DEPENDS libnotify
RDEPENDS = "networkmanager"

inherit gnome gtk-icon-cache

SRC_URI = "svn://svn.gnome.org/svn/network-manager-applet/;module=trunk;proto=http \
           file://applet-no-gnome.diff;patch=1;pnum=0"

PV = "0.0+svnr${SRCREV}"

S = "${WORKDIR}/trunk"

FILES_${PN} += "${datadir}/nm-applet/ \
        ${datadir}/gnome-vpn-properties/ \
        ${datadir}/gnome/autostart/ \
        "
