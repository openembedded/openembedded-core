DESCRIPTION = "Tools for GUPnP"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://src/network-light/main.c;beginline=1;endline=21;md5=033bf37c030780c5a72165846b3003f6"
DEPENDS = "gupnp gupnp-av gtk+ libglade gnome-icon-theme"

SRC_URI = "http://gupnp.org/sites/all/files/sources/${BPN}-${PV}.tar.gz"

PR = "r0"

inherit autotools pkgconfig

SRC_URI[md5sum] = "d8a44a8c19b1cc10b8e5508448d8493f"
SRC_URI[sha256sum] = "3b46a76fcbb0188b8d2c406e514edc7662d65f48774c81e5a19f93d7706db302"
