SUMMARY = "Library to handle input devices in Wayland compositors"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libinput/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=2184aef38ff137ed33ce9a63b9d1eb8f"

DEPENDS = "libevdev udev mtdev"

SRC_URI = "http://www.freedesktop.org/software/${BPN}/${BP}.tar.xz \
           file://touchpad-serial-synaptics-need-to-fake-new-touches-on-TRIPLETAP.patch \
"
SRC_URI[md5sum] = "5f8bc2c746bfbc9454143f78e5b477a9"
SRC_URI[sha256sum] = "998a75fb261d161efaa7da44411cdc9a32a953280e4ffc6322ca19f057d0c1ea"

inherit autotools pkgconfig

PACKAGECONFIG ??= ""
PACKAGECONFIG[libunwind] = "--with-libunwind,--without-libunwind,libunwind"
PACKAGECONFIG[libwacom] = "--enable-libwacom,--disable-libwacom,libwacom"
PACKAGECONFIG[gui] = "--enable-event-gui,--disable-event-gui,cairo gtk+3"

FILES_${PN} += "${libdir}/udev/"
