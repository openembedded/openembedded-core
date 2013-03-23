DESCRIPTION = "The PCRE library is a set of functions that implement regular \
expression pattern matching using the same syntax and semantics as Perl 5. PCRE \
has its own native API, as well as a set of wrapper functions that correspond \
to the POSIX regular expression API."
SUMMARY = "Perl Compatible Regular Expressions"
HOMEPAGE = "http://www.pcre.org"
SECTION = "devel"
PR = "r1"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENCE;md5=115e2bee152e2e23e838a29136094877"
SRC_URI = "${SOURCEFORGE_MIRROR}/project/pcre/pcre/${PV}/pcre-${PV}.tar.bz2 \
           file://pcre-cross.patch \
           file://fix-pcre-name-collision.patch"

SRC_URI[md5sum] = "62f02a76bb57a40bc66681760ed511d5"
SRC_URI[sha256sum] = "a913fb9bd058ef380a2d91847c3c23fcf98e92dc3b47cd08a53c021c5cde0f55"

S = "${WORKDIR}/pcre-${PV}"

FILESPATH .= ":${@base_set_filespath([bb.which(BBPATH, 'recipes-support/libpcre/files', direction=True)], d)}"

PROVIDES += "pcre"
DEPENDS += "bzip2 zlib readline"

inherit autotools binconfig

PARALLEL_MAKE = ""

EXTRA_OECONF = "\
    --enable-newline-is-lf \
    --enable-rebuild-chartables \
    --enable-utf8 \
    --with-link-size=2 \
    --with-match-limit=10000000 \
"

# Set LINK_SIZE in BUILD_CFLAGS given that the autotools bbclass use it to
# set CFLAGS_FOR_BUILD, required for the libpcre build.
BUILD_CFLAGS =+ "-DLINK_SIZE=2 -I${B}"
CFLAGS += "-D_REENTRANT"
CXXFLAGS_append_powerpc = " -lstdc++"

PACKAGES =+ "libpcrecpp libpcreposix pcregrep pcregrep-doc pcretest pcretest-doc"

SUMMARY_libpcrecpp = "${SUMMARY} - C++ wrapper functions"
SUMMARY_libpcreposix = "${SUMMARY} - C wrapper functions based on the POSIX regex API"
SUMMARY_pcregrep = "grep utility that uses perl 5 compatible regexes"
SUMMARY_pcregrep-doc = "grep utility that uses perl 5 compatible regexes - docs"
SUMMARY_pcretest = "program for testing Perl-comatible regular expressions"
SUMMARY_pcretest-doc = "program for testing Perl-comatible regular expressions - docs"

FILES_libpcrecpp = "${libdir}/libpcrecpp.so.*"
FILES_libpcreposix = "${libdir}/libpcreposix.so.*"
FILES_pcregrep = "${bindir}/pcregrep"
FILES_pcregrep-doc = "${mandir}/man1/pcregrep.1"
FILES_pcretest = "${bindir}/pcretest"
FILES_pcretest-doc = "${mandir}/man1/pcretest.1"

BBCLASSEXTEND = "native nativesdk"
