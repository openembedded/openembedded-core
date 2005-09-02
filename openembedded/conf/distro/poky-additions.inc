# Overide some bits

DISTRO = "openzaurus"
DISTRO_NAME = "OpenedHand PokyLinux" # Bad?
DISTRO_VERSION = "0.0-snapshot-${DATE}"

DISTRO_TYPE = "debug"
# DISTRO_TYPE = "release"

PREFERRED_PROVIDER_gconf=gconf-dbus

PREFERRED_VERSION_libmatchbox ?= "1.7"
PREFERRED_VERSION_matchbox ?= "0.9.1"
PREFERRED_VERSION_matchbox-common ?= "0.9.1"
PREFERRED_VERSION_matchbox-desktop ?= "0.9.1"
PREFERRED_VERSION_matchbox-wm ?= "svn"
PREFERRED_VERSION_matchbox-panel ?= "0.9.2"
PREFERRED_VERSION_matchbox-applet-inputmanager ?= "0.6"

PREFERRED_VERSION_atk ?= "1.10.0"
PREFERRED_VERSION_cairo ?= "1.0.0"
PREFERRED_VERSION_glib-2.0 ?= "2.8.1"
PREFERRED_VERSION_gtk+ ?= "2.8.3"
PREFERRED_VERSION_pango ?= "1.10.0"

