LICENSE = "GPL"

DESCRIPTION = "Library for reading some sort of media format."
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = ""

inherit autotools

SRC_URI = "http://liba52.sourceforge.net/files/a52dec-${PV}.tar.gz"
S="${WORKDIR}/a52dec-${PV}"

EXTRA_OECONF = " --enable-shared "

PACKAGES = "${PN} ${PN}-dev a52dec a52dec-doc"

FILES_${PN} = " ${libdir}/liba52.so.0 ${libdir}/liba52.so.0.0.0 " 
FILES_${PN}-dev = " ${includedir}/a52dec/*.h ${libdir}/liba52.so ${libdir}/liba52.la ${libdir}/liba52.a "
FILES_a52dec = " ${bindir}/* "
FILES_a52dec-doc = " ${mandir}/man1/* "

do_stage() {
	oe_libinstall -a -so -C liba52 liba52 ${STAGING_LIBDIR}

	install -d ${STAGING_INCDIR}/a52dec
	install -m 0644 ${S}/include/a52.h ${STAGING_INCDIR}/a52dec/a52.h
	install -m 0644 ${S}/include/attributes.h ${STAGING_INCDIR}/a52dec/attributes.h
	install -m 0644 ${S}/include/audio_out.h ${STAGING_INCDIR}/a52dec/audio_out.h
	install -m 0644 ${S}/include/mm_accel.h ${STAGING_INCDIR}/a52dec/mm_accel.h
}
