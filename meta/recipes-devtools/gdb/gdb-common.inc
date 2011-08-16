DESCRIPTION = "gdb - GNU debugger"
HOMEPAGE = "http://www.gnu.org/software/gdb/"
LICENSE="GPLv3+"
SECTION = "devel"
DEPENDS = "ncurses readline elfutils ${LTTNGUST}"

LTTNGUST = "lttng-ust"
LTTNGUST_libc-uclibc = ""
LTTNGUST_mips = ""
LTTNGUST_mipsel = ""
LTTNGUST_mips64 = ""
LTTNGUST_mips64el = ""

INC_PR = "r3"

LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
		file://COPYING.LIB;md5=9f604d8a4f8e74f4f5140845a21b6674 \
		file://COPYING3;md5=d32239bcb673463ab874e80d47fae504 \
		file://COPYING3.LIB;md5=6a6a8e020838b23406c81b19c1d46df6"
inherit autotools

SRC_URI = "${GNU_MIRROR}/gdb/gdb-${PV}.tar.gz \
           file://no-werror.patch"

SRC_URI[md5sum] = "1d6b4e469270433583ef62a906c02fa8"
SRC_URI[sha256sum] = "d1791d1f9d6c21ec265f4f74dadc15bc35698158a1b836d1626b9a69b818f5c5"

export CC_FOR_BUILD = "${BUILD_CC}"
export CXX_FOR_BUILD = "${BUILD_CXX}"
export CPP_FOR_BUILD = "${BUILD_CPP}"
export CFLAGS_FOR_BUILD = "${BUILD_CFLAGS}"
export CXXFLAGS_FOR_BUILD = "${BUILD_CXXFLAGS}"
export CPPFLAGS_FOR_BUILD = "${BUILD_CPPFLAGS}"

B = "${WORKDIR}/build-${TARGET_SYS}"

EXTRA_OEMAKE = "'SUBDIRS=intl mmalloc libiberty opcodes bfd sim gdb etc utils'"

EXPAT = "--without-expat"

EXTRA_OECONF = "--disable-gdbtk --disable-tui --disable-x \
                --with-curses --disable-multilib --with-system-readline --disable-sim \
                ${GDBPROPREFIX} --with-libelf=${STAGING_DIR_TARGET} ${EXPAT} \
               "

GDBPROPREFIX = "--program-prefix=''"

do_configure () {
	# override this function to avoid the autoconf/automake/aclocal/autoheader
	# calls for now
	(cd ${S} && gnu-configize) || die "failure in running gnu-configize"
	oe_runconf
}

# we don't want gdb to provide bfd/iberty/opcodes, which instead will override the
# right bits installed by binutils.
do_install_append() {
	rm -rf ${D}${libdir}
	rm -rf ${D}${includedir}
	rm -rf ${D}${datadir}/locale
}

RRECOMMENDS_gdb_append_linux = " glibc-thread-db "
RRECOMMENDS_gdb_append_linux-gnueabi = " glibc-thread-db "

