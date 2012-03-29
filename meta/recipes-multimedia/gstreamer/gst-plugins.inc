DESCRIPTION = "Plugins for GStreamer"
HOMEPAGE = "http://gstreamer.freedesktop.org/"
BUGTRACKER = "https://bugzilla.gnome.org/enter_bug.cgi?product=Gstreamer"
SECTION = "multimedia"
DEPENDS = "gstreamer"

inherit autotools pkgconfig

FILESPATH =. "${FILE_DIRNAME}/gst-plugins:"

SRC_URI = "http://gstreamer.freedesktop.org/src/${BPN}/${BPN}-${PV}.tar.bz2"

GSTREAMER_DEBUG ?= "--disable-debug"
EXTRA_OECONF = "--disable-valgrind ${GSTREAMER_DEBUG} --disable-examples "

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

require gst-plugins-package.inc

PACKAGES_DYNAMIC = "${PN}-*"

