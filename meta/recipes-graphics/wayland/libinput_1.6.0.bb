SUMMARY = "Library to handle input devices in Wayland compositors"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libinput/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=2184aef38ff137ed33ce9a63b9d1eb8f"

DEPENDS = "libevdev udev mtdev"

SRC_URI = "http://www.freedesktop.org/software/${BPN}/${BP}.tar.xz \
           file://touchpad-serial-synaptics-need-to-fake-new-touches-on-TRIPLETAP.patch \
"
SRC_URI[md5sum] = "ed9b435d411462d24d88a87728f6c9d6"
SRC_URI[sha256sum] = "b7534f518d735c643aedca2fb4694683dfddc8d0600cfb628c87a18e65255832"

inherit autotools pkgconfig

PACKAGECONFIG ??= ""
PACKAGECONFIG[libunwind] = "--with-libunwind,--without-libunwind,libunwind"
PACKAGECONFIG[libwacom] = "--enable-libwacom,--disable-libwacom,libwacom"
PACKAGECONFIG[gui] = "--enable-event-gui,--disable-event-gui,cairo gtk+3"

UDEVDIR = "`pkg-config --variable=udevdir udev`"

EXTRA_OECONF += "--with-udev-dir=${UDEVDIR}"
