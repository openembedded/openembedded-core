SUMMARY = "Generic font configuration library"
DESCRIPTION = "Fontconfig is a font configuration and customization library, which \
does not depend on the X Window System. It is designed to locate \
fonts within the system and select them according to requirements \
specified by applications. \
Fontconfig is not a rasterization library, nor does it impose a \
particular rasterization library on the application. The X-specific \
library 'Xft' uses fontconfig along with freetype to specify and \
rasterize fonts."

HOMEPAGE = "http://www.fontconfig.org"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=fontconfig"

LICENSE = "MIT-style & MIT & PD"
LIC_FILES_CHKSUM = "file://COPYING;md5=dc5b39c592e47a22dbec44855988d2a0 \
                    file://src/fcfreetype.c;endline=45;md5=5d9513e3196a1fbfdfa94051c09dfc84 \
                    file://src/fccache.c;beginline=1109;endline=1124;md5=0326cfeb4a7333dd4dd25fbbc4b9f27f"

SECTION = "libs"

DEPENDS = "expat freetype zlib"

PR = "r0"

SRC_URI = "http://fontconfig.org/release/fontconfig-${PV}.tar.gz \
           file://fix-pkgconfig.patch \
           file://97_fontconfig"

SRC_URI[md5sum] = "025e08b3d7fe45c433de5718e441ed15"
SRC_URI[sha256sum] = "5c3bf994bb6d6303bbf1e641eaa4b431932138dc90de33642e5845e31e1fdfd6"

PACKAGES =+ "fontconfig-utils-dbg fontconfig-utils "
FILES_fontconfig-utils-dbg = "${bindir}/*.dbg"
FILES_fontconfig-utils = "${bindir}/*"
FILES_${PN} =+ "${datadir}/xml/*"

# Work around past breakage in debian.bbclass
RPROVIDES_fontconfig-utils = "libfontconfig-utils"
RREPLACES_fontconfig-utils = "libfontconfig-utils"
RCONFLICTS_fontconfig-utils = "libfontconfig-utils"
DEBIAN_NOAUTONAME_fontconfig-utils = "1"

PARALLEL_MAKE = ""

inherit autotools pkgconfig

export HASDOCBOOK="no"

EXTRA_OECONF = " --disable-docs --with-arch=${HOST_ARCH} --with-default-fonts=${datadir}/fonts"
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

BUILD_CFLAGS += " -I${STAGING_INCDIR}/freetype2"

do_configure_append () {
	sed -i 's|LDFLAGS =.*|LDFLAGS =|' fc-case/Makefile
	sed -i 's|LDFLAGS =.*|LDFLAGS =|' fc-glyphname/Makefile
	sed -i 's|LDFLAGS =.*|LDFLAGS =|' fc-lang/Makefile

	sed -i 's|CFLAGS =.*|CFLAGS =${BUILD_CFLAGS}|' fc-case/Makefile
	sed -i 's|CFLAGS =.*|CFLAGS =${BUILD_CFLAGS}|' fc-glyphname/Makefile
	sed -i 's|CFLAGS =.*|CFLAGS =${BUILD_CFLAGS}|' fc-lang/Makefile

	sed -i 's|CPPFLAGS =.*|CPPFLAGS =${BUILD_CPPFLAGS}|' fc-case/Makefile
	sed -i 's|CPPFLAGS =.*|CPPFLAGS =${BUILD_CPPFLAGS}|' fc-glyphname/Makefile
	sed -i 's|CPPFLAGS =.*|CPPFLAGS =${BUILD_CPPFLAGS}|' fc-lang/Makefile

	sed -i 's|CXXFLAGS =.*|CFLAGS =${BUILD_CXXFLAGS}|' fc-case/Makefile
	sed -i 's|CXXFLAGS =.*|CFLAGS =${BUILD_CXXFLAGS}|' fc-glyphname/Makefile
	sed -i 's|CXXFLAGS =.*|CFLAGS =${BUILD_CXXFLAGS}|' fc-lang/Makefile

}

do_install_append() {
	install -d ${D}${sysconfdir}/default/volatiles
	install -m 0644 ${WORKDIR}/97_fontconfig ${D}${sysconfdir}/default/volatiles
	rmdir ${D}${localstatedir}/cache/fontconfig
	rmdir ${D}${localstatedir}/cache/
}
