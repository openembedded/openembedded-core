DESCRIPTION = "Perl-compatible regular expression library. PCRE has its own native \
API, but a set of 'wrapper' functions that are based on the POSIX API \
are also supplied in the library libpcreposix. Note that this just \
provides a POSIX calling interface to PCRE; the regular expressions \
themselves still follow Perl syntax and semantics. The header file for \
the POSIX-style functions is called pcreposix.h."
SECTION = "devel"
PR = "r2"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENCE;md5=115e2bee152e2e23e838a29136094877"
SRC_URI = "${SOURCEFORGE_MIRROR}/project/pcre/pcre/${PV}/pcre-${PV}.tar.bz2 \
           file://pcre-cross.patch \
           file://fix-pcre-name-collision.patch"

SRC_URI[md5sum] = "98e8928cccc945d04279581e778fbdff"
SRC_URI[sha256sum] = "c1113fd7db934e97ad8b3917d432e5b642e9eb9afd127eb797804937c965f4ac"

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
    do_split_packages(d, pcre_libdir, '^lib(.*)\.so\.+', 'lib%s', 'libpcre %s library', extra_depends='', allow_links=True, prepend=True)
}

BBCLASSEXTEND = "native"
