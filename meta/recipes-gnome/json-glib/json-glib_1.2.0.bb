SUMMARY = "JSON-GLib implements a full JSON parser using GLib and GObject"
DESCRIPTION = "Use JSON-GLib it is possible to parse and generate valid JSON\
 data structures, using a DOM-like API. JSON-GLib also offers GObject \
integration, providing the ability to serialize and deserialize GObject \
instances to and from JSON data types."
HOMEPAGE = "http://live.gnome.org/JsonGlib"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

DEPENDS = "glib-2.0"

SRC_URI[archive.md5sum] = "efe14b6b8e7aa95ee3240cc60627dc9f"
SRC_URI[archive.sha256sum] = "99d6dfbe49c08fd7529f1fe8dcb1893b810a1bb222f1e7b65f41507658b8a7d3"

inherit gnomebase gettext lib_package gobject-introspection

BBCLASSEXTEND = "native"
