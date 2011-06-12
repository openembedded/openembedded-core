DESCRIPTION = "gtk frontend for connman"
HOMEPAGE = "http://connman.net/"
SECTION = "libs/network"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://properties/main.c;beginline=1;endline=20;md5=50c77c81871308b033ab7a1504626afb \
                    file://common/connman-dbus.c;beginline=1;endline=20;md5=de6b485c0e717a0236402d220187717a"
DEPENDS = "gtk+ dbus"
PR = "r5"

RRECOMMENDS_${PN} = "python python-dbus connman connman-plugin-ethernet connman-plugin-loopback connman-plugin-udhcp connman-plugin-wifi connman-plugin-fake connman-plugin-bluetooth connman-plugin-dnsproxy"

SRCREV = "78d3c39db6f3f7977b466305110faa8ca5f74ec8"
SRC_URI = "git://git.kernel.org/pub/scm/network/connman/connman-gnome.git;protocol=git"
S = "${WORKDIR}/git"

inherit autotools gtk-icon-cache
