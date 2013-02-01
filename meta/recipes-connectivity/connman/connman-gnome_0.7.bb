DESCRIPTION = "gtk frontend for connman"
HOMEPAGE = "http://connman.net/"
SECTION = "libs/network"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://properties/main.c;beginline=1;endline=20;md5=50c77c81871308b033ab7a1504626afb \
                    file://common/connman-dbus.c;beginline=1;endline=20;md5=de6b485c0e717a0236402d220187717a"

DEPENDS = "gtk+ dbus-glib intltool-native"

# 0.7 tag
SRCREV = "cf3c325b23dae843c5499a113591cfbc98acb143"
SRC_URI = "git://github.com/connectivity/connman-gnome.git;protocol=git \
	   file://0001-Removed-icon-from-connman-gnome-about-applet.patch"

S = "${WORKDIR}/git"

inherit autotools gtk-icon-cache

RDEPENDS_${PN} = "connman"
