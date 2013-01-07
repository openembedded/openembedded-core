DESCRIPTION = "A collection of GSettings schemas for settings shared by various components of a desktop"
HOMEPAGE = "http://live.gnome.org/gsettings-desktop-schemas"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"
PR = "r1"

DEPENDS = "glib-2.0"

inherit gnome gsettings gettext

GNOME_COMPRESS_TYPE = "xz"

SRC_URI[archive.md5sum] = "6dbede8f6112b7e0dd4864e979b91771"
SRC_URI[archive.sha256sum] = "1da53e1b960f08ce4b0d472e6f339b7beaf19a7889c15911775abf4efe2ec760"
