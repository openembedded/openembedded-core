DEPENDS = "curl icu libxml2 cairo libxslt libidn gnutls gtk+"


WEBKIT_PORT = "gtk-port"
WEBKIT_EXTRA_OPTIONS = "CONFIG-=qt"

FILES_webkit-gtklauncher = "${bindir}/GtkLauncher"
FILES_webkit-gtklauncher-dbg = "${bindir}/.debug/GtkLauncher"

require webkit.inc

PR = "r3"

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${libdir}
	install -d ${D}${libdir}/pkgconfig

	install -m 0755 ${S}/WebKitBuilds/Debug/WebKitTools/GtkLauncher/GtkLauncher ${D}${bindir}
	cd ${S}/WebKitBuilds/Debug
	PWD=`pwd` ${WEBKIT_QMAKE} WEBKIT_INC_DIR=${D}${prefix}/include WEBKIT_LIB_DIR=${D}${libdir} $PWD/../../WebKit.pro
	oe_runmake install
}

