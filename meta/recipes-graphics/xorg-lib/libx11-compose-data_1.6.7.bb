SUMMARY = "Xlib: Compose data files for libx11"
DESCRIPTION = "This package provides the compose data files for libx11."

python () {
    if bb.utils.contains('DISTRO_FEATURES', 'x11', True, False, d):
        raise bb.parse.SkipRecipe("libx11-compose-data is incompatible with x11 distro feature, use libx11 instead.")
}

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=172255dee66bb0151435b2d5d709fcf7"

SRC_URI[md5sum] = "034fdd6cc5393974d88aec6f5bc96162"
SRC_URI[sha256sum] = "910e9e30efba4ad3672ca277741c2728aebffa7bc526f04dcfa74df2e52a1348"

SRC_URI += "file://0001-Drop-x11-dependencies.patch"

XORG_PN = "libX11"

EXTRA_OECONF += "--disable-xkb"

PACKAGES = "${PN}"

FILES_${PN} = "${datadir}/X11/locale ${libdir}/X11/locale"

do_compile() {
    oe_runmake -C nls
}

do_install() {
    oe_runmake DESTDIR=${D} -C nls install
}

REQUIRED_DISTRO_FEATURES = ""
