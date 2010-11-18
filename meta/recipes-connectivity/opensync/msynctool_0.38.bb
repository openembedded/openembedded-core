SUMMARY = "Calendar (and other PIM data) synchronization program"
DESCRIPTION = "msynctool is a program to synchronize calendars, \
addressbooks and other PIM data between programs on your computer and \
other computers, mobile devices, PDAs or cell phones.  It uses the \
OpenSync plugins when synchronizing data."
HOMEPAGE = "http://www.opensync.org/"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://tools/msynctool.c;beginline=1;endline=20;md5=0b71ef245b75c74bff7d7ec58b9b4527"

DEPENDS = "libopensync glib-2.0"

SRC_URI = "http://www.opensync.org/download/releases/${PV}/msynctool-${PV}.tar.bz2"

inherit cmake pkgconfig

PR = "r0"

SRC_URI[md5sum] = "495c45d6f12d3523a736864b0ced6ce5"
SRC_URI[sha256sum] = "4a903d4db05bf2f677a675ec47e9791da9b1752c9feda0026157e82aa97e372b"
