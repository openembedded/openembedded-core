DESCRIPTION = "FarSight is an audio/video conferencing framework specifically designed for Instant Messengers."
HOMEPAGE = "http://farsight.sf.net"
SRC_URI = "http://farsight.freedesktop.org/releases/farsight2/${P}.tar.gz"

DEPENDS = "libnice glib-2.0 libxml2 zlib dbus gstreamer gst-plugins-base"

inherit autotools_stage
AUTOTOOLS_STAGE_PKGCONFIG = "1"

EXTRA_OECONF = " \
  --disable-debug \
  --disable-gtk-doc \
  --disable-python \
"

FILES_${PN} += "${libdir}/*/*.so"
FILES_${PN}-dev += "${libdir}/*/*a"
FILES_${PN}-dbg += "${libdir}/*/.debug"




