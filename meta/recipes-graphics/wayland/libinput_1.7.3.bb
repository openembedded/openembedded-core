SUMMARY = "Library to handle input devices in Wayland compositors"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libinput/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=2184aef38ff137ed33ce9a63b9d1eb8f"

DEPENDS = "libevdev udev mtdev"

SRC_URI = "http://www.freedesktop.org/software/${BPN}/${BP}.tar.xz \
           file://touchpad-serial-synaptics-need-to-fake-new-touches-on-TRIPLETAP.patch \
"
SRC_URI[md5sum] = "f2993b477db8d7ec0e785ce04ffecb03"
SRC_URI[sha256sum] = "096d612d2711f0caa2de544976ff3729e6233511ab373808644cc2dd5affcb1d"

inherit autotools pkgconfig

PACKAGECONFIG ??= ""
PACKAGECONFIG[libunwind] = "--with-libunwind,--without-libunwind,libunwind"
PACKAGECONFIG[libwacom] = "--enable-libwacom,--disable-libwacom,libwacom"
PACKAGECONFIG[gui] = "--enable-event-gui,--disable-event-gui,cairo gtk+3"

UDEVDIR = "`pkg-config --variable=udevdir udev`"

EXTRA_OECONF += "--with-udev-dir=${UDEVDIR}"
