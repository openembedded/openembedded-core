DESCRIPTION = "Perl is a popular scripting language."
MAINTAINER="David Karlstrom <daka@thg.se>"
HOMEPAGE = "http://www.perl.org/"
LICENSE = "Artistic|GPL"
PR = "r1"

SECTION = "libs"
inherit native

DEPENDS = "virtual/db-native gdbm-native"

SRC_URI = "http://ftp.funet.fi/pub/CPAN/src/perl-${PV}.tar.gz \
           file://Configure-multilib.patch;patch=1"
S = "${WORKDIR}/perl-${PV}"

#perl is not parallel_make safe
PARALLEL_MAKE = ""

do_configure () {
    ./Configure					\
    -Dcc="${BUILD_CC}"				\
    -Dcflags="${BUILD_CFLAGS}"			\
    -Dldflags="${BUILD_LDFLAGS} -Wl,-rpath,${STAGING_LIBDIR}"		\
    -Dusethreads				\
    -Duselargefiles				\
    -Dprefix=${prefix}				\
    -Dvendorprefix=${prefix}			\
    -Dsiteprefix=${prefix}/local		\
    -Dman1ext=1					\
    -Dman3ext=3perl				\
    -Uafs					\
    -Ud_csh					\
    -Uusesfio					\
    -Uusenm -des
    sed 's!${STAGING_DIR}/bin!${STAGING_BINDIR}!; 
         s!${STAGING_DIR}/lib!${STAGING_LIBDIR}!' < config.sh > config.sh.new
    mv config.sh.new config.sh
}
