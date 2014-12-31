SUMMARY = "JSON-GLib implements a full JSON parser using GLib and GObject"
DESCRIPTION = "Use JSON-GLib it is possible to parse and generate valid JSON\
 data structures, using a DOM-like API. JSON-GLib also offers GObject \
integration, providing the ability to serialize and deserialize GObject \
instances to and from JSON data types."
HOMEPAGE = "http://live.gnome.org/JsonGlib"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

DEPENDS = "glib-2.0"

GNOME_COMPRESS_TYPE = "xz"

SRC_URI[archive.md5sum] = "e43efaf6852958207982e79141bf371e"
SRC_URI[archive.sha256sum] = "887bd192da8f5edc53b490ec51bf3ffebd958a671f5963e4f3af32c22e35660a"

inherit gnome gettext lib_package

EXTRA_OECONF = "--disable-introspection"
