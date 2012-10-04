DESCRIPTION = "Sato Icon Theme"
HOMEPAGE = "http://www.o-hand.com"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "CC-BY-SA-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=56a830bbe6e4697fe6cbbae01bb7c2b2"

SECTION = "x11"
DEPENDS = ""

inherit autotools pkgconfig allarch gtk-icon-cache

FILES_${PN} += "${datadir}"

EXTRA_OECONF += "--with-iconmap=${STAGING_LIBDIR_NATIVE}/../libexec/icon-name-mapping"

# Explictly setting "Sato" as the default icon theme to avoid flickering from
# the desktop and settings daemon racing.  This shouldn't be done here but in the sato image
pkg_postinst_${PN} () {
    mkdir -p $D/etc/gtk-2.0

    grep -s -q -e ^gtk-icon-theme-name.*\"Sato\" $D/etc/gtk-2.0/gtkrc || \
        echo 'gtk-icon-theme-name = "Sato"' >> $D/etc/gtk-2.0/gtkrc
}
