DESCRIPTION = "GLib-based library for accessing online service APIs using the GData protocol"
HOMEPAGE = "http://live.gnome.org/libgdata"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24 \
                    file://gdata/gdata.h;endline=20;md5=079a554efcf65d46f96a515806e7e99a \
                    file://gdata/gdata-types.h;endline=20;md5=7399b111aac8718da13888fc634be6ef"

DEPENDS = "libxml2 glib-2.0 libsoup-2.4"

inherit gnome pkgconfig autotools

SRC_URI[archive.md5sum] = "e12f52a3d25c25016856c64ca0331221"
SRC_URI[archive.sha256sum] = "dcb82f7162d69549512444376da2cdea65650ee4dae4d00eed7fbbd3387ddf2c"
