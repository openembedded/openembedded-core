# this build system is mostly shared by attr and acl

SRC_URI += "file://relative-libdir.patch;striplevel=0 \
           "

inherit autotools gettext

# the package comes with a custom config.h.in, it cannot be
# overwritten by autoheader
export AUTOHEADER = "true"
EXTRA_OECONF = "INSTALL_USER=root INSTALL_GROUP=root"

EXTRA_OEMAKE = "PKG_LIB_DIR=${base_libdir} PKG_DEVLIB_DIR=${libdir}"

do_install () {
	oe_runmake install install-lib install-dev DIST_ROOT="${D}"
}

PACKAGES =+ "lib${BPN} lib${BPN}-dev lib${BPN}-doc"

FILES_lib${BPN} = "${base_libdir}/lib*.so.*"

FILES_lib${BPN}-dev = "${includedir} \
                      ${libdir}/lib*.so \
                      ${libdir}/lib*.a \
                      ${libdir}/lib*.la \
                      ${base_libdir}/lib*.so \
                      ${base_libdir}/lib*.a \
                      ${base_libdir}/lib*.la"

FILES_lib${BPN}-doc = "${mandir}/man2 \
                      ${mandir}/man3"

BBCLASSEXTEND = "native"
# Only append ldflags for target recipe and if USE_NLS is enabled
LDFLAGS_append_libc-uclibc = "${@['', ' -lintl '][(bb.data.getVar('PN', d, True) == bb.data.getVar('BPN', d , True)) and (bb.data.getVar('USE_NLS', d, True) == 'yes')]}"
EXTRA_OECONF_append_libc-uclibc = "${@['', ' --disable-gettext '][(bb.data.getVar('PN', d, True) == bb.data.getVar('BPN', d , True)) and (bb.data.getVar('USE_NLS', d, True) == 'no')]}"
