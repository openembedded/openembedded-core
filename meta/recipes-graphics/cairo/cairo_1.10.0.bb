require cairo.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=e73e999e0c72b5ac9012424fa157ad77"

PR = "r1"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz"

SRC_URI[md5sum] = "70a2ece66cf473d976e2db0f75bf199e"
SRC_URI[sha256sum] = "0f2ce4cc4615594088d74eb8b5360bad7c3cc3c3da9b61af9bfd979ed1ed94b2"

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

DESCRIPTION_libcairo2 = "The Cairo 2D vector graphics library"
DESCRIPTION_libcairo-gobject2 = "The Cairo library GObject wrapper library"
DESCRIPTION_libcairo-script-interpreter2 = "The Cairo library script interpreter"
DESCRIPTION_cairo-perf-utils = "The Cairo library performance utilities"

FILES_libcairo2 = "${libdir}/libcairo.so*"
FILES_libcairo2-dev = "${includedir} ${libdir}/*.la ${libdir}/*.a \
					   ${libdir}/*.o ${libdir}/pkgconfig"
FILES_libcairo2-dbg = "${libdir}/.debug ${libdir}/cairo/.debug"
FILES_libcairo-gobject2 = "${libdir}/libcairo-gobject.so*"
FILES_libcairo-script-interpreter2 = "${libdir}/libcairo-script-interpreter.so*"
FILES_cairo-perf-utils = "${bindir}/cairo-trace ${libdir}/cairo/libcairo-trace.*"
