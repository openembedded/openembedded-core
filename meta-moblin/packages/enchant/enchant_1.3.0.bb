DESCRIPTION = "Enchant Spell checker API Library"
SECTION     = "libs"
LICENSE     = "LGPL"
DEPENDS     = "aspell"
RDEPENDS    = "aspell"

inherit autotools pkgconfig

PR = "r1"

S = "${WORKDIR}/enchant-${PV}"

SRC_URI = "http://www.abisource.com/downloads/enchant/${PV}/enchant-${PV}.tar.gz"

EXTRA_OECONF = "--with-aspell-prefix=${STAGING_DIR_HOST}${prefix} --enable-aspell --disable-binreloc"

FILES_${PN} = "${bindir} ${libdir}/*${SOLIBS} ${datadir}/${PN} ${libdir}/${PN}/*.so"
FILES_${PN}-dev += "${libdir}/${PN}/*{SOLIBSDEV} ${libdir}/${PN}/*.la ${libdir}/${PN}/*.a" 

export CXXFLAGS += " -L${STAGING_LIBDIR} -lstdc++ "
