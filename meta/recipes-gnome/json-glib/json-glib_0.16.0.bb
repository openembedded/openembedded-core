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

SRC_URI[archive.md5sum] = "bbca11f32509d6eb3f54d24156e7312d"
SRC_URI[archive.sha256sum] = "e4a3fd2f399e4c148aad608e6ed0a94095f2ddde9dd12f5aa2f072ecae5c1d37"

inherit gnome gettext

EXTRA_OECONF = "--disable-introspection"
