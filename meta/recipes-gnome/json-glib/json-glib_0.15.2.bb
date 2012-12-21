SUMMARY = "JSON-GLib implements a full JSON parser using GLib and GObject."
DESCRIPTION = "Use JSON-GLib it is possible to parse and generate valid JSON\
 data structures, using a DOM-like API. JSON-GLib also offers GObject \
integration, providing the ability to serialize and deserialize GObject \
instances to and from JSON data types."
HOMEPAGE = "http://live.gnome.org/JsonGlib"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

PR = "r0"

DEPENDS = "glib-2.0"

GNOME_COMPRESS_TYPE = "xz"

SRC_URI[archive.md5sum] = "494332526f87956d05f32a9b12f3f286"
SRC_URI[archive.sha256sum] = "f090cd94acc85989e033d72028fa70863d05092ae5bba6b454e70c132b24cdde"

inherit gnome gettext

EXTRA_OECONF = "--disable-introspection"
