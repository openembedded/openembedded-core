require cairo.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=e73e999e0c72b5ac9012424fa157ad77"

PR = "r0"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz"

SRC_URI[md5sum] = "f101a9e88b783337b20b2e26dfd26d5f"
SRC_URI[sha256sum] = "32018c7998358eebc2ad578ff8d8559d34fc80252095f110a572ed23d989fc41"

RDEPENDS_${PN} += "libcairo2 libcairo-gobject2 libcairo-script-interpreter2 \
		  cairo-perf-utils"

ALLOW_EMPTY = "1"

#
# Old version (1.8.10 or below) is automatically named as libcairo2 by
# debian_package_name_hook. To keep backword compatibility,
# it is necessary to explicitly name the package as libcairo2 here
#
PACKAGES =+ "libcairo2 libcairo2-dev libcairo2-dbg \
		     libcairo-gobject2 \
			 libcairo-script-interpreter2 \
		     cairo-perf-utils \
			"

SUMMARY_libcairo2 = "The Cairo 2D vector graphics library"
DESCRIPTION_libcairo2 = "Cairo is a multi-platform library providing anti-aliased \
vector-based rendering for multiple target backends. Paths consist \
of line segments and cubic splines and can be rendered at any width \
with various join and cap styles. All colors may be specified with \
optional translucence (opacity/alpha) and combined using the \
extended Porter/Duff compositing algebra as found in the X Render \
Extension."

SUMMARY_libcairo-gobject2 = "The Cairo library GObject wrapper library"
DESCRIPTION_libcairo-gobject2 = "A GObject wrapper library for the Cairo API."

SUMMARY_libcairo-script-interpreter2 = "The Cairo library script interpreter"
DESCRIPTION_libcairo-script-interpreter2 = "The Cairo script interpreter implements \
CairoScript.  CairoScript is used by tracing utilities to enable the ability \
to replay rendering."

DESCRIPTION_cairo-perf-utils = "The Cairo library performance utilities"

FILES_libcairo2 = "${libdir}/libcairo.so*"
FILES_libcairo2-dev = "${includedir} ${libdir}/*.la ${libdir}/*.a \
					   ${libdir}/*.o ${libdir}/pkgconfig"
FILES_libcairo2-dbg = "${libdir}/.debug ${libdir}/cairo/.debug"
FILES_libcairo-gobject2 = "${libdir}/libcairo-gobject.so*"
FILES_libcairo-script-interpreter2 = "${libdir}/libcairo-script-interpreter.so*"
FILES_cairo-perf-utils = "${bindir}/cairo-trace ${libdir}/cairo/libcairo-trace.*"
