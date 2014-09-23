SUMMARY = "Library to handle input devices in Wayland compositors"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libinput/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=673e626420c7f859fbe2be3a9c13632d"

DEPENDS = "libevdev udev mtdev"

SRC_URI = "http://www.freedesktop.org/software/${BPN}/${BP}.tar.xz"
SRC_URI[md5sum] = "3afaf9f66d8796323a79edb879c10ba3"
SRC_URI[sha256sum] = "30b555771e7cb921ccb9430c4a86940aa3938d05506e81d2417c03e30451bfbc"

inherit autotools pkgconfig
