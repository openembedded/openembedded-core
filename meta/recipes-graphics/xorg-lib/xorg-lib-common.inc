HOMEPAGE = "http://www.x.org"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=xorg"

SECTION = "x11/libs"
LICENSE = "MIT-X"
DEPENDS = "util-macros"

XORG_PN = "${BPN}"

SRC_URI = "${XORG_MIRROR}/individual/lib/${XORG_PN}-${PV}.tar.bz2"

S = "${WORKDIR}/${XORG_PN}-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-specs --without-groff --without-ps2pdf --without-fop --without-xmlto"
EXTRA_OECONF_append_libc-glibc = " --disable-malloc0returnsnull"
EXTRA_OECONF_append_libc-uclibc = " --enable-malloc0returnsnull"

python () {
        whitelist = [ "pixman", "libpciaccess" ]
        if not d.getVar('BPN', True) in whitelist and not oe.utils.contains ('DISTRO_FEATURES', 'x11', True, False, d):
                raise bb.parse.SkipPackage("'x11' not in DISTRO_FEATURES")
}
