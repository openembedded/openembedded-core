DEPENDS = "curl icu libxml2 cairo libxslt libidn gnutls gtk+ gstreamer gst-plugins-base gnome-vfs flex-native gperf-native perl-native sqlite3"

SRCREV_FORMAT = "webcore-rwebkit"

# Yes, this is wrong...
PV = "0.1+svnr${SRCREV}"
PR = "r4"

SRC_URI = "\
  svn://svn.webkit.org/repository/webkit/trunk/;module=JavaScriptCore;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=JavaScriptGlue;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebCore;proto=http;name=webcore \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKit;proto=http;name=webkit \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKitLibraries;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKitTools;proto=http \
  file://Makefile \
  file://Makefile.shared \
  file://autogen.sh \
  file://configure.ac \
  file://GNUmakefile.am \
 "

S = "${WORKDIR}/"

inherit autotools pkgconfig

EXTRA_OECONF = "\
                --enable-debug=no \
                --enable-svg \
                --enable-icon-database=yes \
               "

do_compile_prepend() {
        mkdir -p ${S}/WebKitBuilds/Debug/JavaScriptCore/pcre/tmp/
        mkdir -p ${S}/Programs/
	cd ${S}/JavaScriptCore/pcre
        if test -e dftables.c
        then
            ${BUILD_CC} dftables.c -o dftables -I. -I../wtf
        elif test -e dftables.cpp
        then
            ${BUILD_CXX} dftables.cpp -o dftables -I. -I../wtf
        fi
        cp dftables ${S}/WebKitBuilds/Debug/JavaScriptCore/pcre/tmp/
        cp dftables ${S}/Programs/        
	cd ${S}
}

do_stage() {
	autotools_stage_all
}

PACKAGES =+ "${PN}launcher-dbg ${PN}launcher libjavascriptcore"
FILES_${PN}launcher = "${bindir}/GtkLauncher"
FILES_${PN}launcher-dbg = "${bindir}/.debug/GtkLauncher"
FILES_libjavascriptcore = "${libdir}/libJavaScriptCore.so.*"



