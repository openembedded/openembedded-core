SUMMARY = "GNU Troff software"
DESCRIPTION = "The groff (GNU troff) software is a typesetting package which reads plain text mixed with \
formatting commands and produces formatted output."
SECTION = "base"
HOMEPAGE = "https://www.gnu.org/software/groff/"
LICENSE = "GPL-3.0-or-later"

LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "${GNU_MIRROR}/groff/groff-${PV}.tar.gz \
           file://groff-not-search-fonts-on-build-host.patch \
           file://0001-Make-manpages-mulitlib-identical.patch \
           file://0001-groff-fix-test-groff-not-found-in-cross-compilation.patch \
           file://0001-m4-groff.m4-GROFF_G-don-t-prefix-when-troff-fails-to-.patch \
           "

SRC_URI:append:class-native = "file://build-less.patch"

SRC_URI[sha256sum] = "24d103a07eb79e418963bce206c8771b649871f83bda2fd4c2e65529c1c56921"

DEPENDS = "bison-native groff-native"
RDEPENDS:${PN} += "perl sed"

inherit autotools-brokensep texinfo multilib_script pkgconfig

MULTILIB_SCRIPTS = "${PN}:${bindir}/gpinyin ${PN}:${bindir}/grog"

EXTRA_OECONF = "--without-x --with-urw-fonts-dir=/completely/bogus/dir/"
EXTRA_OEMAKE:class-target = "GROFFBIN=groff GROFF_BIN_PATH=${STAGING_BINDIR_NATIVE} SOELIMBIN=${STAGING_BINDIR_NATIVE}/soelim"

CACHED_CONFIGUREVARS += "ac_cv_path_PERL='/usr/bin/env perl' ac_cv_path_BASH_PROG='no' PAGE=A4"

EXTRANATIVEPATH:class-target += "perl-native"

# Delete these generated files since we depend on bison-native
# and regenerate them. Do it deterministically (always).
do_configure:prepend() {
	rm -f ${S}/src/preproc/eqn/eqn.cpp
	rm -f ${S}/src/preproc/eqn/eqn.hpp
}

do_install:append() {
	# Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
	# for target as /usr/bin/perl, so fix it to /usr/bin/perl.
	for i in afmtodit mmroff gropdf pdfmom grog; do
		if [ -f ${D}${bindir}/$i ]; then
			sed -i -e '1s,#!.*perl,#! ${USRBINPATH}/env perl,' ${D}${bindir}/$i
		fi
	done
	if [ -e ${D}${libdir}/charset.alias ]; then
		rm -rf ${D}${libdir}/charset.alias
	fi

	# awk is located at /usr/bin/, not /bin/
	SPECIAL_AWK=`find ${D} -name special.awk`
	if [ -f ${SPECIAL_AWK} ]; then
		sed -i -e 's:#!.*awk:#! ${USRBINPATH}/awk:' ${SPECIAL_AWK}
	fi

	# not ship /usr/bin/glilypond and its releated files in embedded target system
	rm -rf ${D}${bindir}/glilypond
	rm -rf ${D}${libdir}/groff/glilypond
	rm -rf ${D}${mandir}/man1/glilypond*

	# not ship /usr/bin/grap2graph and its releated man files
	rm -rf ${D}${bindir}/grap2graph
	rm -rf ${D}${mandir}/man1/grap2graph*

	# strip hosttool path out of generated files
	if [ -d ${D}${docdir}/${BP}/examples/hdtbl/ ]; then
		sed -i -e 's:${HOSTTOOLS_DIR}/::g' ${D}${docdir}/${BP}/examples/hdtbl/*.roff
	fi

	# Remove build paths from generated groff.7 for reproducibility.
	if [ -f ${D}${mandir}/man7/groff.7 ]; then
		sed -i -e 's:${S}/::g' ${D}${mandir}/man7/groff.7
	fi
}

do_install:append:class-native() {
	create_cmdline_wrapper ${D}/${bindir}/groff \
		-F${STAGING_DIR_NATIVE}${datadir_native}/groff/${PV}/font \
		-M${STAGING_DIR_NATIVE}${datadir_native}/groff/${PV}/tmac
}

FILES:${PN} += "${libdir}/${BPN}/site-tmac \
                ${libdir}/${BPN}/groffer/"

BBCLASSEXTEND = "native"
