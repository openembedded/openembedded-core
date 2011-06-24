DESCRIPTION = "WebKitGTK+ is the port of the portable web rendering engine WebKitK to the GTK+ platform."
HOMEPAGE = "http://www.webkitgtk.org/"
BUGTRACKER = "http://bugs.webkit.org/"

LICENSE = "BSD & LGPLv2+"
LIC_FILES_CHKSUM = "file://WebCore/rendering/RenderApplet.h;endline=22;md5=fb9694013ad71b78f8913af7a5959680 \
                    file://WebKit/gtk/webkit/webkit.h;endline=21;md5=b4fbe9f4a944f1d071dba1d2c76b3351 \
                    file://JavaScriptCore/parser/Parser.h;endline=23;md5=2f3cff0ad0a9c486da5a376928973a90"

DEPENDS = "enchant gnome-keyring libsoup-2.4 curl icu libxml2 cairo libxslt libxt libidn gnutls gtk+ gstreamer gst-plugins-base flex-native gperf-native perl-native-runtime sqlite3"
DEPENDS_darwin8 = "curl icu libxml2 cairo libxslt libidn gnutls gtk+ gstreamer flex-native gperf-native perl-native-runtime sqlite3"

SRCREV_FORMAT = "webcore-rwebkit"

SRCREV = "72836"
PV = "1.3.7+svnr${SRCPV}"
PR = "r2"

SRC_URI = "\
  svn://svn.webkit.org/repository/webkit/trunk/;module=JavaScriptCore;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=JavaScriptGlue;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebCore;proto=http;name=webcore \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKit;proto=http;name=webkit \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKitLibraries;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=WebKitTools;proto=http \
  svn://svn.webkit.org/repository/webkit/trunk/;module=autotools;proto=http \
  file://Makefile \
  file://Makefile.shared \
  file://autogen.sh \
  file://configure.ac \
  file://GNUmakefile.am \
  file://gtk-doc.make \
  file://nodolt.patch \
 "

S = "${WORKDIR}/"

inherit autotools lib_package pkgconfig

EXTRA_OECONF = "\
                --enable-debug=no \
                --enable-svg \
                --enable-icon-database=yes \
		--disable-fast-malloc \
               "

EXTRA_AUTORECONF = " -I autotools "

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

do_install_prepend() {
        cp ${S}/Programs/.libs/jsc ${S}/Programs/jsc-1 || true
}


PACKAGES =+ "${PN}-webinspector ${PN}launcher-dbg ${PN}launcher libjavascriptcore"
FILES_${PN}launcher = "${bindir}/GtkLauncher"
FILES_${PN}launcher-dbg = "${bindir}/.debug/GtkLauncher"
FILES_libjavascriptcore = "${libdir}/libJavaScriptCore.so.*"
FILES_${PN}-webinspector = "${datadir}/webkitgtk-*/webinspector/"
FILES_${PN} += "${datadir}/webkit-*/resources/error.html \
                ${datadir}/webkitgtk-*/images \
                ${datadir}/glib-2.0/schemas"


