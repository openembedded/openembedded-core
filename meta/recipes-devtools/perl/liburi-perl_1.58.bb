DESCRIPTION = "This package contains the URI.pm module with friends. \
The module implements the URI class. URI objects can be used to access \
and manipulate the various components that make up these strings."

SECTION = "libs"
LICENSE = "Artistic | GPLv1+"
PR = "r2"

LIC_FILES_CHKSUM = "file://README;beginline=26;endline=30;md5=6c33ae5c87fd1c4897714e122dd9c23d"

DEPENDS += "perl"

SRC_URI = "http://www.cpan.org/authors/id/G/GA/GAAS/URI-${PV}.tar.gz"

SRC_URI[md5sum] = "540575aee18616ad9a21e0af7a1e7b18"
SRC_URI[sha256sum] = "5ddeb8e4707bd4c37a3a60f634de8424e40c85bba98cf6c1053ae5e71c9b8289"

S = "${WORKDIR}/URI-${PV}"

EXTRA_CPANFLAGS = "EXPATLIBPATH=${STAGING_LIBDIR} EXPATINCPATH=${STAGING_INCDIR}"

inherit cpan

do_compile() {
	export LIBC="$(find ${STAGING_DIR_TARGET}/${base_libdir}/ -name 'libc-*.so')"
	cpan_do_compile
}

