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

SRC_URI[archive.md5sum] = "0c6121741956fc34933a7ebae5868ec2"
SRC_URI[archive.sha256sum] = "a95475364ec27ab1d2a69303cf579018558bfb6981e3498b3aaf1e6401f7422c"

inherit gnome gettext

EXTRA_OECONF = "--disable-introspection"
