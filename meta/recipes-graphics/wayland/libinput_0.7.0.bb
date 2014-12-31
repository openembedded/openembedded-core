SUMMARY = "Library to handle input devices in Wayland compositors"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libinput/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=673e626420c7f859fbe2be3a9c13632d"

DEPENDS = "libevdev udev mtdev"

SRC_URI = "http://www.freedesktop.org/software/${BPN}/${BP}.tar.xz"
SRC_URI[md5sum] = "381b61396de28c12716ef7a5475fea50"
SRC_URI[sha256sum] = "129f485afe5e4a9394641293991c97cb99f5f3338340d0d65b704ff463d1579e"

inherit autotools pkgconfig
