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
                    file://src/fccache.c;beginline=1182;endline=1197;md5=0326cfeb4a7333dd4dd25fbbc4b9f27f"

SECTION = "libs"

DEPENDS = "expat freetype zlib"

PR = "r1"

SRC_URI = "http://fontconfig.org/release/fontconfig-${PV}.tar.gz \
           file://Add-sysroot-option-to-fc-cache-and-fc-cat.patch \
          "

SRC_URI[md5sum] = "025e08b3d7fe45c433de5718e441ed15"
SRC_URI[sha256sum] = "5c3bf994bb6d6303bbf1e641eaa4b431932138dc90de33642e5845e31e1fdfd6"

PACKAGES =+ "fontconfig-utils"
FILES_${PN} =+ "${datadir}/xml/*"
FILES_fontconfig-utils = "${bindir}/*"

# Work around past breakage in debian.bbclass
RPROVIDES_fontconfig-utils = "libfontconfig-utils"
RREPLACES_fontconfig-utils = "libfontconfig-utils"
RCONFLICTS_fontconfig-utils = "libfontconfig-utils"
DEBIAN_NOAUTONAME_fontconfig-utils = "1"

inherit autotools pkgconfig

EXTRA_OECONF = " --disable-docs --with-default-fonts=${datadir}/fonts"

BBCLASSEXTEND = "native"
