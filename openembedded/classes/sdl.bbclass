FILES_${PN} += '${libdir}/perl5'

sdl_do_configure () {
        if [ -x ${S}/configure ] ; then
                cfgcmd="${S}/configure \
                    -GL -GLU"
                oenote "Running $cfgcmd..."
                $cfgcmd || oefatal "oe_runconf failed"
		if [ "${BUILD_SYS}" != "${HOST_SYS}" ]; then
			. ${STAGING_DIR}/${TARGET_SYS}/perl/config.sh
			sed -e "s:\(SITELIBEXP = \).*:\1${sitelibexp}:; s:\(SITEARCHEXP = \).*:\1${sitearchexp}:; s:\(INSTALLVENDORLIB = \).*:\1${D}${libdir}/perl5:; s:\(INSTALLVENDORARCH = \).*:\1${D}${libdir}/perl5:" < Makefile > Makefile.new
			mv Makefile.new Makefile
		fi
        else
                oefatal "no configure script found"
        fi
}

sdl_do_compile () {
        oe_runmake PASTHRU_INC="${CFLAGS}"
}

sdl_do_install () {
	oe_runmake install_vendor
}

EXPORT_FUNCTIONS do_configure do_compile do_install
