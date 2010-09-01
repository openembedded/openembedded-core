DESCRIPTION = "Enchant Spell checker API Library"
SECTION     = "libs"
HOMEPAGE = "http://www.abisource.com/projects/enchant/"
BUGTRACKER = "http://bugzilla.abisource.com/enter_bug.cgi?product=Enchant"
LICENSE     = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=dfcbabf9131dc9a95febef6753a2958b \
                    file://src/enchant.h;beginline=1;endline=29;md5=8d881caa1d37523567e1d8f726675a18"
DEPENDS     = "aspell glib-2.0"
RDEPENDS    = "aspell"

inherit autotools pkgconfig

PR = "r0"

SRC_URI = "http://www.abisource.com/downloads/enchant/${PV}/enchant-${PV}.tar.gz"

EXTRA_OECONF = "--with-aspell-prefix=${STAGING_DIR_HOST}${prefix} --enable-aspell --disable-binreloc"

FILES_${PN} = "${bindir} ${libdir}/*${SOLIBS} ${datadir}/${PN} ${libdir}/${PN}/*.so"
FILES_${PN}-dev += "${libdir}/${PN}/*{SOLIBSDEV} ${libdir}/${PN}/*.la ${libdir}/${PN}/*.a" 

export CXXFLAGS += " -L${STAGING_LIBDIR} -lstdc++ "
