DESCRIPTION = "gtk frontend for connman"
HOMEPAGE = "http://connman.net/"
SECTION = "libs/network"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://properties/main.c;beginline=1;endline=20;md5=50c77c81871308b033ab7a1504626afb \
                    file://common/connman-dbus.c;beginline=1;endline=20;md5=de6b485c0e717a0236402d220187717a"
DEPENDS = "gtk+ dbus"
PR = "r4"

RRECOMMENDS_${PN} = "python python-dbus connman connman-plugin-ethernet connman-plugin-loopback connman-plugin-udhcp connman-plugin-wifi connman-plugin-fake connman-plugin-bluetooth connman-plugin-dnsproxy"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/network/connman/connman-gnome-${PV}.tar.gz \
           file://connman-gnome.patch \
           file://remove-connman-property-desktop-file.patch \
           file://service_name_update.patch \
           file://applet_menu_popup_fix.patch"

SRC_URI[md5sum] = "0e1c4c25d19cad9b08a216848a320716"
SRC_URI[sha256sum] = "4d5fe481b444fc3e64fd9caa149dbcd76de166a25733f18fd93da01d2abf5d1c"

inherit autotools gtk-icon-cache
