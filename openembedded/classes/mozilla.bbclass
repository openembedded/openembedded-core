SECTION = "x11/utils"
DEPENDS += "gnu-config-native virtual/libintl xt libxi \
	    zip-native gtk+ orbit2 libidl-native"
LICENSE = "MPL NPL"
SRC_URI += "file://mozconfig"

inherit gettext

EXTRA_OECONF = "--target=${TARGET_SYS} --host=${BUILD_SYS} \
		--build=${BUILD_SYS} --prefix=${prefix}"
EXTRA_OEMAKE = "'HOST_LIBIDL_LIBS=${HOST_LIBIDL_LIBS}' \
		'HOST_LIBIDL_CFLAGS=${HOST_LIBIDL_CFLAGS}'"
SELECTED_OPTIMIZATION = "-Os -fsigned-char -fno-strict-aliasing"

export CROSS_COMPILE = "1"
export MOZCONFIG = "${WORKDIR}/mozconfig"
export MOZ_OBJDIR = "${S}"

export CONFIGURE_ARGS = "${EXTRA_OECONF}"
export HOST_LIBIDL_CFLAGS = "`${HOST_LIBIDL_CONFIG} --cflags`"
export HOST_LIBIDL_LIBS = "`${HOST_LIBIDL_CONFIG} --libs`"
export HOST_LIBIDL_CONFIG = "PKG_CONFIG_PATH=${STAGING_BINDIR}/../share/pkgconfig pkg-config libIDL-2.0"
export HOST_CC = "${BUILD_CC}"
export HOST_CXX = "${BUILD_CXX}"
export HOST_CFLAGS = "${BUILD_CFLAGS}"
export HOST_CXXFLAGS = "${BUILD_CXXFLAGS}"
export HOST_LDFLAGS = "${BUILD_LDFLAGS}"
export HOST_RANLIB = "${BUILD_RANLIB}"
export HOST_AR = "${BUILD_AR}"

mozilla_do_configure() {
	(
		set -e
		for cg in `find ${S} -name config.guess`; do
			install -m 0755 \
			${STAGING_BINDIR}/../share/gnu-config/config.guess \
			${STAGING_BINDIR}/../share/gnu-config/config.sub \
			`dirname $cg`/
		done
	)
	oe_runmake -f client.mk ${MOZ_OBJDIR}/Makefile \
				${MOZ_OBJDIR}/config.status
}

mozilla_do_compile() {
	oe_runmake -f client.mk build_all
}

mozilla_do_install() {
	oe_runmake DESTDIR="${D}" destdir="${D}" install
}

EXPORT_FUNCTIONS do_configure do_compile do_install
