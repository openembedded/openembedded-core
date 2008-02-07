require gcc-cross_${PV}.bb
require gcc-cross-initial.inc

S = "${WORKDIR}/gcc-2006q1"

EXTRA_OECONF += "--disable-libmudflap \
		--disable-libssp"
