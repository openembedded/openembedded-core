DESCRIPTION = "A minimal version of the Mozilla web browser for mobile devices"
SECTION = "x11/network"
LICENSE = "MPL/GPL/LGPL"
HOMEPAGE = "http://www.mozilla.org/projects/minimo/"
PRIORITY = "optional"

DEPENDS = "libxrender xt xft fontconfig freetype libidl dbus-glib pango atk gtk+"

CVSSVR="cvs-mirror.mozilla.org"
BRTAG = "MOZILLA_1_8_BRANCH"
MOZDATE = "20060720"

PV = "0.016+cvs${MOZDATE}"
PR = "r5"

SRC_URI = "cvs://anonymous@${CVSSVR}/cvsroot;module=mozilla;tag=${BRTAG};date=${MOZDATE} \
	   file://minimo.patch;patch=1 \
           file://bug-322806.diff;patch=1 \
	   file://mozconfig file://minimo \
	   file://eabi-fix.patch;patch=1 \
	   file://eabi-fix2.patch;patch=1 \
	   file://eabi-fix3.patch;patch=1 \
           file://minimo.desktop file://minimo.png"

inherit autotools

S = "${WORKDIR}/mozilla"

export MOZCONFIG = "${WORKDIR}/mozconfig"

export CROSS_COMPILE="1"
export ac_cv_prog_HOST_CC="${BUILD_CC}"
export ac_cv_prog_HOST_CFLAGS="${BUILD_CFLAGS}"
export ac_cv_prog_HOST_CXX="${BUILD_CXX}"
export ac_cv_prog_HOST_CXXFLAGS="${BUILD_CXXFLAGS}"

mozdir="${libdir}/mozilla-minimo"

EXTRA_OECONF += "--build=${BUILD_SYS} --host=${BUILD_SYS} --target=${TARGET_SYS} "

do_configure() {
	cd ${S}
	oe_runmake -f client.mk CONFIGURE_ARGS="${EXTRA_OECONF}" configure
	sed -i s:${TARGET_PREFIX}strip:echo:g config/autoconf.mk
}

do_compile() {
	cd ${S}
	oe_runmake -f client.mk build
}

do_install() {
	cd ${WORKDIR}

	install -d ${D}${bindir}
	install -m 0755 minimo ${D}${bindir}

	install -d ${D}${datadir}/applications
	install -m 0644 minimo.desktop ${D}${datadir}/applications

	install -d ${D}/${datadir}/pixmaps
	install -m 0644 minimo.png ${D}${datadir}/pixmaps

	cd ${S}

	./minimo/config/linux_package.sh ${S} ${S}/minimo/config

	cd dist/minimo

	install -d ${D}${mozdir}
	install -m 0755 minimo ${D}${mozdir}
	install -m 0755 libfreebl3.so ${D}${mozdir}
	install -m 0755 libnspr4.so ${D}${mozdir}
	install -m 0755 libnss3.so ${D}${mozdir}
	install -m 0755 libnssckbi.so ${D}${mozdir}
	install -m 0755 libplc4.so ${D}${mozdir}
	install -m 0755 libplds4.so ${D}${mozdir}
	install -m 0755 libsmime3.so ${D}${mozdir}
	install -m 0755 libsoftokn3.so ${D}${mozdir}
	install -m 0755 libssl3.so ${D}${mozdir}

	install -d ${D}${mozdir}/chrome
	install -m 0644 chrome/classic.jar ${D}${mozdir}/chrome
	install -m 0644 chrome/classic.manifest ${D}${mozdir}/chrome
	install -m 0644 chrome/en-US.jar ${D}${mozdir}/chrome
	install -m 0644 chrome/en-US.manifest ${D}${mozdir}/chrome
	install -m 0644 chrome/minimo-skin-vga.jar ${D}${mozdir}/chrome
	install -m 0644 chrome/minimo-skin-vga.manifest ${D}${mozdir}/chrome
	install -m 0644 chrome/minimo-skin.jar ${D}${mozdir}/chrome
	install -m 0644 chrome/minimo-skin.manifest ${D}${mozdir}/chrome
	install -m 0644 chrome/minimo.jar ${D}${mozdir}/chrome
	install -m 0644 chrome/minimo.manifest ${D}${mozdir}/chrome
	install -m 0644 chrome/pippki.jar ${D}${mozdir}/chrome
	install -m 0644 chrome/pippki.manifest ${D}${mozdir}/chrome
	install -m 0644 chrome/toolkit.jar ${D}${mozdir}/chrome
	install -m 0644 chrome/toolkit.manifest ${D}${mozdir}/chrome

	install -d ${D}${mozdir}/components
	install -m 0644 components/all.xpt ${D}${mozdir}/components
	install -m 0644 components/nsHelperAppDlg.js ${D}${mozdir}/components
	install -m 0644 components/nsProgressDialog.js ${D}${mozdir}/components

	install -d ${D}${mozdir}/greprefs
	install -m 0644 greprefs/all.js ${D}${mozdir}/greprefs
	install -m 0644 greprefs/security-prefs.js ${D}${mozdir}/greprefs

	install -d ${D}${mozdir}/res
	install -m 0644 res/forms.css ${D}${mozdir}/res
	install -m 0644 res/html.css ${D}${mozdir}/res
	install -m 0644 res/quirk.css ${D}${mozdir}/res
	install -m 0644 res/ua.css ${D}${mozdir}/res
	install -m 0644 res/arrow.gif ${D}${mozdir}/res
	install -m 0644 res/arrowd.gif ${D}${mozdir}/res
	install -m 0644 res/broken-image.gif ${D}${mozdir}/res
	install -m 0644 res/loading-image.gif ${D}${mozdir}/res
	install -m 0644 res/charsetData.properties ${D}${mozdir}/res
	install -m 0644 res/charsetalias.properties ${D}${mozdir}/res
	install -m 0644 res/langGroups.properties ${D}${mozdir}/res
	install -m 0644 res/language.properties ${D}${mozdir}/res
	install -m 0644 res/unixcharset.properties ${D}${mozdir}/res

	install -d ${D}${mozdir}/res/dtd
	install -m 0644 res/dtd/xhtml11.dtd ${D}${mozdir}/res/dtd

	install -d ${D}${mozdir}/res/entityTables
	install -m 0644 res/entityTables/html40Latin1.properties ${D}${mozdir}/res/entityTables
	install -m 0644 res/entityTables/html40Special.properties ${D}${mozdir}/res/entityTables
	install -m 0644 res/entityTables/html40Symbols.properties ${D}${mozdir}/res/entityTables
	install -m 0644 res/entityTables/htmlEntityVersions.properties ${D}${mozdir}/res/entityTables
	install -m 0644 res/entityTables/transliterate.properties ${D}${mozdir}/res/entityTables

	install -d ${D}${mozdir}/res/fonts
	install -m 0644 res/fonts/fontEncoding.properties ${D}${mozdir}/res/fonts
	install -m 0644 res/fonts/pangoFontEncoding.properties ${D}${mozdir}/res/fonts

	install -d ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-audio.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-binary.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-find.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-image.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-menu.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-movie.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-sound.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-telnet.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-text.gif ${D}${mozdir}/res/html
	install -m 0644 res/html/gopher-unknown.gif ${D}${mozdir}/res/html
}

FILES_${PN}-dbg += "${libdir}/mozilla-minimo/.debug*"
FILES_${PN} += "${mozdir}"
