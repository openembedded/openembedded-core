SUMMARY = "JSON-GLib implements a full JSON parser using GLib and GObject."
DESCRIPTION = "Use JSON-GLib it is possible to parse and generate valid JSON\
 data structures, using a DOM-like API. JSON-GLib also offers GObject \
integration, providing the ability to serialize and deserialize GObject \
instances to and from JSON data types."
HOMEPAGE = "http://live.gnome.org/JsonGlib"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

PR = "r1"

DEPENDS = "glib-2.0"

SRC_URI[archive.md5sum] = "5716a181a2b85268f53a1fb0c8024c4a"
SRC_URI[archive.sha256sum] = "b62cb148ae49d30d8ad807912ba3c7cf189459e2d75233620aae411cf8ea6c04"

inherit gnome gettext

EXTRA_OECONF = "--disable-introspection"
