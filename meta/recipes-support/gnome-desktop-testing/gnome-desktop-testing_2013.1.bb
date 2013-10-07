SUMMARY = "Test runner for GNOME-style installed tests"
HOMEPAGE = "https://wiki.gnome.org/GnomeGoals/InstalledTests"
LICENSE = "LGPLv2+"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${PV}/${BPN}-${PV}.tar.xz \
           file://no-introspection.patch"
SRC_URI[md5sum] = "c6824f7bfac95bf0fcf6ed0c255979c1"
SRC_URI[sha256sum] = "632e7224de8614a8e4b7cdf87fc32551531efa9290fba0da4dae56234c584b7b"

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://src/gnome-desktop-testing-runner.c;endline=19;md5=ab86a2e00ab9cbb94f008d785004b181"

DEPENDS = "glib-2.0"

inherit autotools pkgconfig

PACKAGECONFIG ??= ""
PACKAGECONFIG[journald] = "--with-systemd-journal,--without-systemd-journal,systemd,systemd"
