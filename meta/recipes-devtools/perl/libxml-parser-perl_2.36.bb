SECTION = "libs"
LICENSE = "Artistic"
DEPENDS += "expat expat-native"

SRC_URI = "http://www.cpan.org/modules/by-module/XML/XML-Parser-${PV}.tar.gz"

S = "${WORKDIR}/XML-Parser-${PV}"

EXTRA_CPANFLAGS = "EXPATLIBPATH=${STAGING_LIBDIR} EXPATINCPATH=${STAGING_INCDIR}"

inherit cpan

do_compile() {
	export LIBC="$(find ${STAGING_DIR_TARGET}/${base_libdir}/ -name 'libc-*.so')"
	cpan_do_compile
}



FILES_${PN} = "${PERLLIBDIRS}/auto/XML/Parser/Expat/* \
                ${PERLLIBDIRS}/XML"
