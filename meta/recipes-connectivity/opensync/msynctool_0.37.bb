SRC_URI = "http://www.opensync.org/download/releases/${PV}/msynctool-${PV}.tar.bz2"

SUMMARY = "Calendar (and other PIM data) synchronization program"

DESCRIPTION = "msynctool is a program to synchronize calendars, \
addressbooks and other PIM data between programs on your computer and \
other computers, mobile devices, PDAs or cell phones.  It uses the \
OpenSync plugins when synchronizing data."

LICENSE = "GPL"
DEPENDS = "libopensync glib-2.0"
HOMEPAGE = "http://www.opensync.org/"

inherit cmake pkgconfig
