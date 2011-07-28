DESCRIPTION = "Poppler is a PDF rendering library based on the xpdf-3.0 code base."
DEPENDS = "fontconfig jpeg zlib gtk+ cairo"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://poppler.freedesktop.org/${BPN}-${PV}.tar.gz"

inherit autotools pkgconfig

EXTRA_OECONF = " --enable-xpdf-headers \
		 --disable-gtk-test \
		 --disable-poppler-qt --disable-poppler-qt4 \
		 --enable-zlib \
	       "

#check for TARGET_FPU=soft and inform configure of the result so it can disable some floating points
require poppler-fpu.inc
EXTRA_OECONF += "${@get_poppler_fpu_setting(bb, d)}"
