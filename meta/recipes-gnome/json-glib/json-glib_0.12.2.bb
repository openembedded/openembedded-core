SUMMARY = "JSON-GLib implements a full JSON parser using GLib and GObject."
DESCRIPTION = "Use JSON-GLib it is possible to parse and generate valid JSON\
 data structures, using a DOM-like API. JSON-GLib also offers GObject \
integration, providing the ability to serialize and deserialize GObject \
instances to and from JSON data types."
HOMEPAGE = "http://live.gnome.org/JsonGlib"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

DEPENDS = "glib-2.0"

SRC_URI[archive.md5sum] = "71f911329c9f742543b378c3494490da"
SRC_URI[archive.sha256sum] = "89fa9b870dfe2d8b7b00210be76cdbb2d46a8d2cc77ca7cbe5d081bdf8cad780"

PR = "r0"

inherit gnome

EXTRA_OECONF = "--disable-introspection"
