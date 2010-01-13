require mutter.inc

SRC_URI = "http://download.moblin.org/sources/mutter/2.28/mutter-2.28.1_0.0.tar.bz2 \
           file://nodocs.patch;patch=1 \
           file://nozenity.patch;patch=1 \
           file://fix_pkgconfig.patch;patch=1 \
           "

S = "${WORKDIR}/mutter-2.28.1_0.0"