SUMMARY = "GNOME library for reading .desktop files"
SECTION = "x11/gnome"
LICENSE = "GPLv2 & LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LIB;md5=5f30f0716dfdd0d91eb439ebec522ec2"

BPN = "gnome-desktop"

inherit gnome pkgconfig upstream-version-is-even gobject-introspection
SRC_URI[archive.md5sum] = "3e7b67578307220a21688f28307b6789"
SRC_URI[archive.sha256sum] = "f1df71c39e32147f6d58f53a9c05b964b00b7c98fbca090419355437c72fd59d"

SRC_URI += " \
           file://gnome-desktop-thumbnail-don-t-assume-time_t-is-long.patch \
           file://0001-Disable-libseccomp-sycall-filtering-mechanism.patch \
           "

DEPENDS += "intltool-native gsettings-desktop-schemas gconf virtual/libx11 gtk+3 glib-2.0 startup-notification xkeyboard-config iso-codes udev"

inherit distro_features_check gtk-doc
REQUIRED_DISTRO_FEATURES = "x11"

EXTRA_OECONF = "--disable-desktop-docs"

PACKAGES =+ "libgnome-desktop3"
FILES_libgnome-desktop3 = "${libdir}/lib*${SOLIBS} ${datadir}/libgnome-desktop*/pnp.ids ${datadir}/gnome/*xml"

RRECOMMENDS_libgnome-desktop3 += "gsettings-desktop-schemas"
