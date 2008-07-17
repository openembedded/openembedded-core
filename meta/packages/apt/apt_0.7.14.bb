DEPENDS = "curl db"
RDEPENDS = "dpkg"

require apt.inc

PR = "r2"

SRC_URI += "file://nodoc.patch;patch=1"

require apt-package.inc

FILES_${PN} += "${bindir}/apt-key"
apt-manpages += "doc/apt-key.8"

do_stage() {
	install -d ${STAGING_LIBDIR} ${STAGING_INCDIR}/apt-pkg
	eval `cat environment.mak | grep ^GLIBC_VER | sed -e's, = ,=,'`
	oe_libinstall -so -C bin libapt-pkg$GLIBC_VER-6 ${STAGING_LIBDIR}/
	ln -sf libapt-pkg$GLIBC_VER-6.so ${STAGING_LIBDIR}/libapt-pkg.so
	oe_libinstall -so -C bin libapt-inst$GLIBC_VER-6 ${STAGING_LIBDIR}/
	ln -sf libapt-inst$GLIBC_VER-6.so ${STAGING_LIBDIR}/libapt-inst.so

	install -m 0644 include/apt-pkg/*.h ${STAGING_INCDIR}/apt-pkg/
}
