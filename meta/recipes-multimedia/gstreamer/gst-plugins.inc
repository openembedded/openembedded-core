DESCRIPTION = "Plugins for GStreamer"
HOMEPAGE = "http://gstreamer.freedesktop.org/"
BUGTRACKER = "https://bugzilla.gnome.org/enter_bug.cgi?product=Gstreamer"
SECTION = "multimedia"
DEPENDS = "gstreamer"

inherit autotools pkgconfig

FILESPATH =. "${FILE_DIRNAME}/gst-plugins:"

SRC_URI = "http://gstreamer.freedesktop.org/src/${BPN}/${BPN}-${PV}.tar.bz2"

EXTRA_OECONF = "--disable-valgrind --disable-debug --disable-examples "

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

# orc.m4 calls pkg-config ----variable=orcc orc-0.4 to get the path to orcc,
# resulting in /usr/bin/orcc. Force it to use the staged orcc.
do_configure_append() {
 	for i in $(find ${S} -name "Makefile") ; do
		sed -i -e s:${bindir}/orcc:${STAGING_BINDIR_NATIVE}/orcc:g $i
 	done
}

require gst-plugins-package.inc

PACKAGES_DYNAMIC = "${PN}-*"

