# this build system is mostly shared by attr and acl

SRC_URI += "file://relative-libdir.patch;striplevel=0 \
            file://no-fixed-prog-path.patch"

inherit autotools gettext

# the package comes with a custom config.h.in, it cannot be
# overwritten by autoheader
export AUTOHEADER = "true"
EXTRA_OECONF = "INSTALL_USER=root INSTALL_GROUP=root"

EXTRA_OEMAKE = "PKG_LIB_DIR=${base_libdir} PKG_DEVLIB_DIR=${libdir}"

do_install () {
	oe_runmake install install-lib install-dev DIST_ROOT="${D}"
}

PACKAGES =+ "lib${PN} lib${PN}-dev lib${PN}-doc"

FILES_lib${PN} = "${base_libdir}/lib*.so.*"

FILES_lib${PN}-dev = "${includedir} \
                      ${libdir}/lib*.so \
                      ${libdir}/lib*.a \
                      ${libdir}/lib*.la \
                      ${base_libdir}/lib*.so \
                      ${base_libdir}/lib*.a \
                      ${base_libdir}/lib*.la"

FILES_lib${PN}-doc = "${mandir}/man2 \
                      ${mandir}/man3"

BBCLASSEXTEND = "native"
