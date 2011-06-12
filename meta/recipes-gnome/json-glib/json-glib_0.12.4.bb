SUMMARY = "JSON-GLib implements a full JSON parser using GLib and GObject."
DESCRIPTION = "Use JSON-GLib it is possible to parse and generate valid JSON\
 data structures, using a DOM-like API. JSON-GLib also offers GObject \
integration, providing the ability to serialize and deserialize GObject \
instances to and from JSON data types."
HOMEPAGE = "http://live.gnome.org/JsonGlib"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

DEPENDS = "glib-2.0"

SRC_URI[archive.md5sum] = "347e1714e4a2ce54298969d5ffec7dca"
SRC_URI[archive.sha256sum] = "462cd611016ae189d5e3f258dc7741e6a2e8267404b4e3806aaf346d50f1df7e"

PR = "r0"

inherit gnome

EXTRA_OECONF = "--disable-introspection"
