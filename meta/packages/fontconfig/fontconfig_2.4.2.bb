SECTION = "libs"
LICENSE = "BSD"
DESCRIPTION = "A library for configuring and customizing font access."
DEPENDS = "expat freetype freetype-native zlib"

SRC_URI = "http://fontconfig.org/release/fontconfig-${PV}.tar.gz"

PR = "r0"

PACKAGES =+ "fontconfig-utils-dbg fontconfig-utils "
FILES_fontconfig-utils-dbg = "${bindir}/*.dbg"
FILES_fontconfig-utils = "${bindir}/*"

# Work around past breakage in debian.bbclass
RPROVIDES_fontconfig-utils = "libfontconfig-utils"
RREPLACES_fontconfig-utils = "libfontconfig-utils"
RCONFLICTS_fontconfig-utils = "libfontconfig-utils"
DEBIAN_NOAUTONAME_fontconfig-utils = "1"

S = "${WORKDIR}/fontconfig-${PV}"

inherit autotools pkgconfig

export HASDOCBOOK="no"

EXTRA_OECONF = " --disable-docs --with-arch=${HOST_ARCH}"
EXTRA_OEMAKE = "FC_LANG=fc-lang FC_GLYPHNAME=fc-glyphname"

# The tarball has some of the patched files as read only, which
# patch doesn't like at all

fontconfig_do_unpack() {
       chmod -R u+rw ${S}
}

python do_unpack () {
       bb.build.exec_func('base_do_unpack', d)
       bb.build.exec_func('fontconfig_do_unpack', d)
}

do_stage () {
	oe_libinstall -so -a -C src libfontconfig ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/fontconfig
	for i in ${S}/fontconfig/*.h; do install -m 0644 $i ${STAGING_INCDIR}/fontconfig/; done
}

BUILD_CFLAGS += " -I${STAGING_DIR}/${BUILD_SYS}/include/freetype2"

do_configure_append () {
	sed -i 's|LDFLAGS =.*|LDFLAGS =|' fc-case/Makefile
	sed -i 's|LDFLAGS =.*|LDFLAGS =|' fc-glyphname/Makefile
	sed -i 's|LDFLAGS =.*|LDFLAGS =|' fc-lang/Makefile
	sed -i 's|LDFLAGS =.*|LDFLAGS =|' fc-arch/Makefile

	sed -i 's|CFLAGS =.*|CFLAGS =${BUILD_CFLAGS}|' fc-case/Makefile
	sed -i 's|CFLAGS =.*|CFLAGS =${BUILD_CFLAGS}|' fc-glyphname/Makefile
	sed -i 's|CFLAGS =.*|CFLAGS =${BUILD_CFLAGS}|' fc-lang/Makefile
	sed -i 's|CFLAGS =.*|CFLAGS =${BUILD_CFLAGS}|' fc-arch/Makefile

	sed -i 's|CPPFLAGS =.*|CPPFLAGS =${BUILD_CPPFLAGS}|' fc-case/Makefile
	sed -i 's|CPPFLAGS =.*|CPPFLAGS =${BUILD_CPPFLAGS}|' fc-glyphname/Makefile
	sed -i 's|CPPFLAGS =.*|CPPFLAGS =${BUILD_CPPFLAGS}|' fc-lang/Makefile
	sed -i 's|CPPFLAGS =.*|CPPFLAGS =${BUILD_CPPFLAGS}|' fc-arch/Makefile

	sed -i 's|CXXFLAGS =.*|CFLAGS =${BUILD_CXXFLAGS}|' fc-case/Makefile
	sed -i 's|CXXFLAGS =.*|CFLAGS =${BUILD_CXXFLAGS}|' fc-glyphname/Makefile
	sed -i 's|CXXFLAGS =.*|CFLAGS =${BUILD_CXXFLAGS}|' fc-lang/Makefile
	sed -i 's|CXXFLAGS =.*|CFLAGS =${BUILD_CXXFLAGS}|' fc-arch/Makefile

}

do_install () {
	autotools_do_install
}

