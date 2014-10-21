SUMMARY = "An sh-compatible command language interpreter"
HOMEPAGE = "http://tiswww.case.edu/php/chet/bash/bashtop.html"
SECTION = "base/shell"

DEPENDS = "ncurses bison-native"

inherit autotools-brokensep gettext texinfo update-alternatives ptest

PARALLEL_MAKE = ""

EXTRA_OECONF = "--enable-job-control"

ALTERNATIVE_${PN} = "sh"
ALTERNATIVE_LINK_NAME[sh] = "${base_bindir}/sh"
ALTERNATIVE_TARGET[sh] = "${base_bindir}/bash"
ALTERNATIVE_PRIORITY = "100"

export AUTOHEADER = "true"

RDEPENDS_${PN} += "base-files"
RDEPENDS_${PN}_class-nativesdk = ""
RDEPENDS_${PN}-ptest += "make"

do_configure_prepend () {
	if [ ! -e ${S}/acinclude.m4 ]; then
		cat ${S}/aclocal.m4 > ${S}/acinclude.m4
	fi
}

do_compile_ptest () {
	oe_runmake buildtest
}

do_install_append () {
	# Move /usr/bin/bash to /bin/bash, if need
	if [ "${base_bindir}" != "${bindir}" ]; then
		mkdir -p ${D}${base_bindir}
		mv ${D}${bindir}/bash ${D}${base_bindir}
	fi
}
do_install_append_class-target () {
	# Clean host path in bashbug
	sed -i -e "s,${STAGING_DIR_TARGET},,g" ${D}${bindir}/bashbug
}

do_install_ptest () {
	make INSTALL_TEST_DIR=${D}${PTEST_PATH}/tests install-test
	cp ${B}/Makefile ${D}${PTEST_PATH}
        sed -i 's/^Makefile/_Makefile/' ${D}${PTEST_PATH}/Makefile
}

pkg_postinst_${PN} () {
	grep -q "^${base_bindir}/bash$" $D${sysconfdir}/shells || echo ${base_bindir}/bash >> $D${sysconfdir}/shells
}

pkg_postrm_${PN} () {
	printf "$(grep -v "^${base_bindir}/bash$" $D${sysconfdir}/shells)\n" > $D${sysconfdir}/shells
}
