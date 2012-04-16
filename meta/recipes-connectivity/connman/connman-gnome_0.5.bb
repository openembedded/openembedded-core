DESCRIPTION = "gtk frontend for connman"
HOMEPAGE = "http://connman.net/"
SECTION = "libs/network"
LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
                    file://properties/main.c;beginline=1;endline=20;md5=50c77c81871308b033ab7a1504626afb \
                    file://common/connman-dbus.c;beginline=1;endline=20;md5=de6b485c0e717a0236402d220187717a"

DEPENDS = "gtk+ dbus"

PR = "r10"

SRCREV = "42c652d123ea133d0a0930b96e8e39dbd0c597b5"
SRC_URI = "git://git.kernel.org/pub/scm/network/connman/connman-gnome.git \
           file://0001-Monitor-the-Manager-s-State-property.patch \
           file://0002-Update-for-ConnMan-0.79-API-changes.patch \
           file://0003-Fix-setting-IPv4-configuration.patch \
           file://0004-Handle-WiFi-authentication-using-an-agent.patch \
           file://0005-Remove-all-handling-of-Passphrase-property.patch \
           file://0006-Fix-status-descriptions-in-properties-tree.patch \
           file://0007-connman-gnome-fix-segfault-due-to-unchecked-null-val.patch"

S = "${WORKDIR}/git"

inherit autotools gtk-icon-cache

RRECOMMENDS_${PN} = "python  \
                     python-dbus \
                     connman \
                     connman-plugin-ethernet \
                     connman-plugin-loopback \
                     connman-plugin-udhcp \
                     connman-plugin-wifi \
                     connman-plugin-fake \
                     connman-plugin-bluetooth \
                     connman-plugin-dnsproxy \
                     connman-plugin-ofono \
                    "
