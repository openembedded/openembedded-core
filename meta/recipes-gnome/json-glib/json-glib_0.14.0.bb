SUMMARY = "JSON-GLib implements a full JSON parser using GLib and GObject."
DESCRIPTION = "Use JSON-GLib it is possible to parse and generate valid JSON\
 data structures, using a DOM-like API. JSON-GLib also offers GObject \
integration, providing the ability to serialize and deserialize GObject \
instances to and from JSON data types."
HOMEPAGE = "http://live.gnome.org/JsonGlib"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

DEPENDS = "glib-2.0"

SRC_URI[archive.md5sum] = "6ba14cc2cc9582250451ff645c20a779"
SRC_URI[archive.sha256sum] = "efa6a22711c99208feef3bdcd6692573640ac7635725417024980b80990a2966"

inherit gnome gettext

EXTRA_OECONF = "--disable-introspection"
