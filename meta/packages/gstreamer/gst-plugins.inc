DESCRIPTION = "Plugins for GStreamer"
SECTION = "multimedia"
PRIORITY = "optional"
DEPENDS = "gstreamer"

inherit autotools pkgconfig

SRC_URI = "http://gstreamer.freedesktop.org/src/${PN}/${PN}-${PV}.tar.bz2"

EXTRA_OECONF = "--disable-valgrind --disable-debug --disable-examples "

acpaths = "-I ${S}/common/m4 -I ${S}/m4"

LIBV = "0.10"
PACKAGES_DYNAMIC = "gst-plugin-*"
#FILES_${PN} += "${libdir}/gstreamer-${LIBV}/*.so"
#FILES_${PN}-dev += "${libdir}/gstreamer-${LIBV}/*.la ${libdir}/gstreamer-${LIBV}/*.a"
FILES_${PN}-dbg += "${libdir}/gstreamer-${LIBV}/.debug"

python populate_packages_prepend () {
	gst_libdir = bb.data.expand('${libdir}/gstreamer-${LIBV}', d)
	postinst = bb.data.getVar('plugin_postinst', d, 1)

#	Can't package separate debug packages yet
#	do_split_packages(d, gst_libdir, '\.debug/libgst(.*)\.so$', 'gst-plugin-%s-dbg', 'GStreamer plugin for %s (with debugging symbols)', recursive=True, extra_depends=bb.data.expand('${PN}-dbg', d), match_path=True)
	do_split_packages(d, gst_libdir, 'libgst(.*)\.so$', 'gst-plugin-%s', 'GStreamer plugin for %s', postinst=postinst, extra_depends=bb.data.expand('${PN}',d))
	do_split_packages(d, gst_libdir, 'libgst(.*)\.l?a$', 'gst-plugin-%s-dev', 'GStreamer plugin for %s (development files)', extra_depends=bb.data.expand('${PN}-dev',d))
}

do_stage() {
	autotools_stage_all
}

ALLOW_EMPTY = "1"
