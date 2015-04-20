SUMMARY = "Error - Error/exception handling in an OO-ish way"
DESCRIPTION = "The Error package provides two interfaces. Firstly \
Error provides a procedural interface to exception handling. \
Secondly Error is a base class for errors/exceptions that can \
either be thrown, for subsequent catch, or can simply be recorded."

SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"

LIC_FILES_CHKSUM = "file://LICENSE;md5=8f3499d09ee74a050c0319391ff9d100"

DEPENDS += "perl"

SRC_URI = "http://cpan.metacpan.org/authors/id/S/SH/SHLOMIF/Error-${PV}.tar.gz"

SRC_URI[md5sum] = "98524ffbd268013e00697a5826a83d37"
SRC_URI[sha256sum] = "af48e19077d8837c6377d8a9ba2ce473c100caf5eeb7b53606c3f78a0e8b5d37"

S = "${WORKDIR}/Error-${PV}"

inherit cpan

do_compile() {
	export LIBC="$(find ${STAGING_DIR_TARGET}/${base_libdir}/ -name 'libc-*.so')"
	cpan_do_compile
}

BBCLASSEXTEND = "native"
