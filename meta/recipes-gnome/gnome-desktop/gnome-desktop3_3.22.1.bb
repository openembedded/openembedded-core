SUMMARY = "GNOME library for reading .desktop files"
SECTION = "x11/gnome"
LICENSE = "GPLv2 & LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LIB;md5=5f30f0716dfdd0d91eb439ebec522ec2"

BPN = "gnome-desktop"

inherit gnome pkgconfig upstream-version-is-even gobject-introspection
SRC_URI[archive.md5sum] = "bc09d58d7870d6464f20803850cdb90f"
SRC_URI[archive.sha256sum] = "6458add4fc3a81fbd9a63db90de22a1e3a62644c1bfd1aca042c43836195aab2"

DEPENDS += "intltool-native gsettings-desktop-schemas gconf libxrandr virtual/libx11 gtk+3 glib-2.0 startup-notification xkeyboard-config iso-codes"

inherit distro_features_check gtk-doc
REQUIRED_DISTRO_FEATURES = "x11"

EXTRA_OECONF = "--disable-desktop-docs"

PACKAGES =+ "libgnome-desktop3"
FILES_libgnome-desktop3 = "${libdir}/lib*${SOLIBS} ${datadir}/libgnome-desktop*/pnp.ids ${datadir}/gnome/*xml"

RRECOMMENDS_libgnome-desktop3 += "gsettings-desktop-schemas"
