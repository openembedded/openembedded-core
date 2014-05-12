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

SRC_URI[archive.md5sum] = "d13485f5aa3b93227bbeb689ccfb596c"
SRC_URI[archive.sha256sum] = "dbf558d2da989ab84a27e4e13daa51ceaa97eb959c2c2f80976c9322a8f4cdde"

inherit gnome gettext lib_package

EXTRA_OECONF = "--disable-introspection"
