DEPENDS += "flex-native gperf-native perl-native sqlite3"
SRCREV_FORMAT = "webcore-rwebkit"

PACKAGES =+ "${PN}launcher-dbg ${PN}launcher"

# Yes, this is wrong...
PV = "0.1+svnr${SRCREV}"

inherit qmake2 pkgconfig

SRC_URI = "\
  svn://svn.webkit.org/repository/webkit/trunk/;module=JavaScriptCore;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=JavaScriptGlue;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebCore;proto=http;name=webcore \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKit;proto=http;name=webkit \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKitLibraries;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKitTools;proto=http \
  file://Makefile \
  file://Makefile.shared \
  file://WebKit.pri \
  file://WebKit.pro \
"
S = "${WORKDIR}/"

WEBKIT_QMAKE = "qmake2 -spec ${QMAKESPEC} -r OUTPUT_DIR=$PWD/ ${WEBKIT_EXTRA_OPTIONS} CONFIG+=${WEBKIT_PORT}"

do_configure() {
	qmake2 -spec ${QMAKESPEC} CONFIG+=${WEBKIT_PORT} ${WEBKIT_EXTRA_OPTIONS} CONFIG-=release CONFIG+=debug
	mkdir -p WebKitBuilds/Debug
	cd WebKitBuilds/Debug
	PWD=`pwd` ${WEBKIT_QMAKE}  WEBKIT_INC_DIR=${prefix}/include WEBKIT_LIB_DIR=${libdir} $PWD/../../WebKit.pro
}

do_compile_prepend() {
	mkdir -p ${S}/WebKitBuilds/Debug/JavaScriptCore/pcre/tmp/
	cd ${S}/JavaScriptCore/pcre 
	if test -e dftables.c
	then
	    ${BUILD_CC} dftables.c -o dftables -I. -I../wtf
	elif test -e dftables.cpp
	then
	    ${BUILD_CXX} dftables.cpp -o dftables -I. -I../wtf
	fi
	cp dftables ${S}/WebKitBuilds/Debug/JavaScriptCore/pcre/tmp/
	cd ${S}/WebKitBuilds/Debug
}

do_stage() {
	install -d ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}
	cd ${S}/WebKitBuilds/Debug
	PWD=`pwd` ${WEBKIT_QMAKE} WEBKIT_INC_DIR=${STAGING_INCDIR} WEBKIT_LIB_DIR=${STAGING_LIBDIR} $PWD/../../WebKit.pro
	oe_runmake install
}

