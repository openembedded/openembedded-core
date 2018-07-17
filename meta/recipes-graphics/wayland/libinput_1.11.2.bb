SUMMARY = "Library to handle input devices in Wayland compositors"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/libinput/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=1f2ea9ebff3a2c6d458faf58492efb63"

DEPENDS = "libevdev udev mtdev"

SRC_URI = "http://www.freedesktop.org/software/${BPN}/${BP}.tar.xz \
           "
SRC_URI[md5sum] = "d179b5afbf414b34ce36ffe6ea8a401d"
SRC_URI[sha256sum] = "6b36e1163d306c292ec8359b35aa65431cd29d0a5254923775e8019ff5018107"

UPSTREAM_CHECK_REGEX = "libinput-(?P<pver>\d+\.\d+\.(?!9\d+)\d+)"
inherit meson pkgconfig lib_package

PACKAGECONFIG ??= ""
PACKAGECONFIG[libwacom] = "-Dlibwacom=true,-Dlibwacom=false,libwacom"
PACKAGECONFIG[gui] = "-Ddebug-gui=true,-Ddebug-gui=false,cairo gtk+3"

UDEVDIR = "`pkg-config --variable=udevdir udev`"

EXTRA_OEMESON += "-Dudev-dir=${UDEVDIR} -Ddocumentation=false -Dtests=false"

# package name changed in 1.8.1 upgrade: make sure package upgrades work
RPROVIDES_${PN} = "libinput"
RREPLACES_${PN} = "libinput"
RCONFLICTS_${PN} = "libinput"
