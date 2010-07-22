DESCRIPTION = "XCB: an X protocol C binding"
HOMEPAGE = "http://xcb.freedesktop.org/XcbUtil/"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=XCB"

LICENSE = "MIT"

SECTION = "x11/libs"

DEPENDS = "libxcb"
DEPENDS += "gperf-native"

PARALLEL_MAKE = ""

SRC_URI = "http://xcb.freedesktop.org/dist/xcb-util-${PV}.tar.bz2"

PACKAGES =+ "libxcbatom libxcbaux libxcbevent libxcbicccm \
             libxcbimage libxcbkeysyms libxcbproperty \
             libxcbreply libxcbwm"

FILES_libxcbatom = "${libdir}/libXCBAtom.so.*"
FILES_libxcbaux = "${libdir}/libXCBAux.so.*"
FILES_libxcbevent = "${libdir}/libXCBEvent.so.*"
FILES_libxcbicccm = "${libdir}/libXCBICCCM.so.*"
FILES_libxcbimage = "${libdir}/libXCBImage.so.*"
FILES_libxcbkeysyms = "${libdir}/libXCBKeysyms.so.*"
FILES_libxcbproperty = "${libdir}/libXCBProperty.so.*"
FILES_libxcbreply = "${libdir}/libXCBReply.so.*"
FILES_libxcbwm = "${libdir}/libXCBWM.so.*"

inherit autotools pkgconfig
