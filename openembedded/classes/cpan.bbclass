FILES_${PN} += '${libdir}/perl5'

cpan_do_configure () {
	perl Makefile.PL
	if [ "${BUILD_SYS}" != "${HOST_SYS}" ]; then
		. ${STAGING_DIR}/${TARGET_SYS}/perl/config.sh
		sed -e "s:\(SITELIBEXP = \).*:\1${sitelibexp}:; s:\(SITEARCHEXP = \).*:\1${sitearchexp}:; s:\(INSTALLVENDORLIB = \).*:\1${D}${libdir}/perl5:; s:\(INSTALLVENDORARCH = \).*:\1${D}${libdir}/perl5:" < Makefile > Makefile.new
		mv Makefile.new Makefile
	fi
}

cpan_do_compile () {
        oe_runmake PASTHRU_INC="${CFLAGS}"
}

cpan_do_install () {
	oe_runmake install_vendor
}

EXPORT_FUNCTIONS do_configure do_compile do_install
