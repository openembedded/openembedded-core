inherit distutils-base

distutils_do_compile() {
	BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
	${STAGING_BINDIR}/python setup.py build || \
	oefatal "python setup.py build execution failed."
}

distutils_do_install() {
	BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
	${STAGING_BINDIR}/python setup.py install --prefix=${D}/${prefix} --install-data=${D}/${datadir} || \
	oefatal "python setup.py install execution failed."
}

EXPORT_FUNCTIONS do_compile do_install
