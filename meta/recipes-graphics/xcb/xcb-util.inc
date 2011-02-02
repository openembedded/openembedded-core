SUMMARY = "XCB: The X protocol C binding utilities"
DESCRIPTION = "The xcb-util module provides a number of libraries which \
sit on top of libxcb, the core X protocol library, and some of the \
extension libraries. These experimental libraries provide convenience \
functions and interfaces which make the raw X protocol more usable. Some \
of the libraries also provide client-side code which is not strictly \
part of the X protocol but which have traditionally been provided by \
Xlib."
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
