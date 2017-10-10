SUMMARY = "Library to handle input devices in Wayland compositors"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libinput/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=2184aef38ff137ed33ce9a63b9d1eb8f"

DEPENDS = "libevdev udev mtdev"

SRC_URI = "http://www.freedesktop.org/software/${BPN}/${BP}.tar.xz"

SRC_URI[md5sum] = "5265244853ad7cf41232b0d3ee805ab9"
SRC_URI[sha256sum] = "2fe2e2f52f0971a9c43541b8f26582ca8df6ed4bb9050e85eb40d4ff6b13142d"

inherit autotools pkgconfig lib_package

PACKAGECONFIG ??= ""
PACKAGECONFIG[libunwind] = "--with-libunwind,--without-libunwind,libunwind"
PACKAGECONFIG[libwacom] = "--enable-libwacom,--disable-libwacom,libwacom"
PACKAGECONFIG[gui] = "--enable-debug-gui,--disable-debug-gui,cairo gtk+3"

UDEVDIR = "`pkg-config --variable=udevdir udev`"

EXTRA_OECONF += "--with-udev-dir=${UDEVDIR} --disable-documentation --disable-tests"

# package name changed in 1.8.1 upgrade: make sure package upgrades work
RPROVIDES_${PN} = "libinput"
RREPLACES_${PN} = "libinput"
RCONFLICTS_${PN} = "libinput"
