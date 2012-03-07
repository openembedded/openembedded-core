DESCRIPTION = "Perl-compatible regular expression library. PCRE has its own native \
API, but a set of 'wrapper' functions that are based on the POSIX API \
are also supplied in the library libpcreposix. Note that this just \
provides a POSIX calling interface to PCRE; the regular expressions \
themselves still follow Perl syntax and semantics. The header file for \
the POSIX-style functions is called pcreposix.h."
SECTION = "devel"
PR = "r1"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENCE;md5=2f00d186a775c8002422a5315079d96b"
SRC_URI = "${SOURCEFORGE_MIRROR}/pcre/pcre-${PV}.tar.bz2 \
           file://pcre-cross.patch \
           file://fix-pcre-name-collision.patch"

SRC_URI[md5sum] = "0a7b592bea64b7aa7f4011fc7171a730"
SRC_URI[sha256sum] = "a4b8509d11fc2764fb4e1415b764ad2c214459edc011ce48aeeb6bbe1ac599e3"

S = "${WORKDIR}/pcre-${PV}"

PROVIDES = "pcre"
DEPENDS = "bzip2 zlib readline"

inherit autotools binconfig

PARALLEL_MAKE = ""

CFLAGS_append = " -D_REENTRANT"
CXXFLAGS_powerpc += "-lstdc++"
EXTRA_OECONF = " --with-link-size=2 --enable-newline-is-lf --with-match-limit=10000000 --enable-rebuild-chartables --enable-utf8"

do_compile () {
	# stop libtool from trying to link with host libraries - fix from #33
	# this resolve build problem on amd64 - #1015
	if [ -e ${S}/${HOST_SYS}-libtool ] ; then
		sed -i 's:-L\$:-L${STAGING_LIBDIR} -L\$:' ${S}/${HOST_SYS}-libtool
	else
		ln -sf ${S}/libtool ${S}/${HOST_SYS}-libtool
		sed -i 's:-L\$:-L${STAGING_LIBDIR} -L\$:' ${S}/${HOST_SYS}-libtool
	fi

	# The generation of dftables can lead to timestamp problems with ccache
	# because the generated config.h seems newer.  It is sufficient to ensure that the
	# attempt to build dftables inside make will actually work (foo_FOR_BUILD is
	# only used for this).
	oe_runmake CC_FOR_BUILD="${BUILD_CC}" CFLAGS_FOR_BUILD="-DLINK_SIZE=2 -I${S}/include" LINK_FOR_BUILD="${BUILD_CC} -L${S}/lib"
}

python populate_packages_prepend () {
	pcre_libdir = d.expand('${libdir}')
	do_split_packages(d, pcre_libdir, '^lib(.*)\.so\.+', 'lib%s', 'libpcre %s library', extra_depends='', allow_links=True)
}

FILES_${PN} = "${libdir}/libpcre.so.*"
FILES_${PN}-dev += "${bindir}/*"

BBCLASSEXTEND = "native"
