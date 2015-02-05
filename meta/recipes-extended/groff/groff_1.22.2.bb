SUMMARY = "GNU Troff software"
DESCRIPTION = "The groff (GNU troff) software is a typesetting package which reads plain text mixed with \
formatting commands and produces formatted output."
SECTION = "base"
HOMEPAGE = "ftp://ftp.gnu.org/gnu/groff/"
LICENSE = "GPLv3"
PR = "r1"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "${GNU_MIRROR}/groff/groff-${PV}.tar.gz \
	file://groff-1.22.2-correct-man.local-install-path.patch \
"

SRC_URI[md5sum] = "9f4cd592a5efc7e36481d8d8d8af6d16"
SRC_URI[sha256sum] = "380864dac4772e0c0d7b1282d25d0c5fd7f63baf45c87c4657afed22a13d2076"

DEPENDS = "groff-native"
DEPENDS_class-native = ""

inherit autotools texinfo

EXTRA_OECONF = "--without-x"
PARALLEL_MAKE = ""

do_configure_prepend() {
	if [ "${BUILD_SYS}" != "${HOST_SYS}" ]; then
		sed -i \
		    -e '/^GROFFBIN=/s:=.*:=${STAGING_BINDIR_NATIVE}/groff:' \
		    -e '/^TROFFBIN=/s:=.*:=${STAGING_BINDIR_NATIVE}/troff:' \
		    -e '/^GROFF_BIN_PATH=/s:=.*:=${STAGING_BINDIR_NATIVE}:' \
		    -e '/^GROFF_BIN_DIR=/s:=.*:=${STAGING_BINDIR_NATIVE}:' \
		    ${S}/contrib/*/Makefile.sub \
		    ${S}/doc/Makefile.in \
		    ${S}/doc/Makefile.sub
	fi
}

do_configure_append() {
    # generate gnulib configure script
    olddir=`pwd`
    cd ${S}/src/libs/gnulib/
    ACLOCAL="$ACLOCAL" autoreconf -Wcross --verbose --install --force ${EXTRA_AUTORECONF} $acpaths || bbfatal "autoreconf execution failed."
    cd ${olddir}
}

do_install_append() {
	# Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
	# for target as /usr/bin/perl, so fix it to /usr/bin/perl.
	for i in afmtodit mmroff gropdf pdfmom; do
		if [ -f ${D}${bindir}/$i ]; then
			sed -i -e '1s,#!.*perl,#! ${USRBINPATH}/env perl,' ${D}${bindir}/$i
		fi
	done
}

do_install_append_class-native() {
	create_cmdline_wrapper ${D}/${bindir}/groff \
		-F${STAGING_DIR_NATIVE}${datadir_native}/groff/${PV}/font \
		-M${STAGING_DIR_NATIVE}${datadir_native}/groff/${PV}/tmac
}

FILES_${PN} += "${libdir}/${BPN}/site-tmac \
                ${libdir}/${BPN}/groffer/"

RDEPENDS_${PN} = " sed"

BBCLASSEXTEND = "native"
