DEPENDS = "curl icu libxml2 cairo libxslt libidn gnutls gtk+"

# If you activate HTML5 media support (ENABLE_VIDEO=1) you'll need:
# DEPENDS += " gstreamer gst-plugins-base gnome-vfs "


WEBKIT_PORT = "gtk-port"
WEBKIT_EXTRA_OPTIONS = "CONFIG-=qt"

FILES_webkit-gtklauncher = "${bindir}/GtkLauncher"
FILES_webkit-gtklauncher-dbg += "${bindir}/.debug/GtkLauncher"

require webkit.inc

SRC_URI += "file://autogen.sh \
            file://configure.ac \
            file://GNUmakefile.am \
	   "

PR = "r4"

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${libdir}
	install -d ${D}${libdir}/pkgconfig

	install -m 0755 ${S}/WebKitBuilds/Debug/WebKitTools/GtkLauncher/GtkLauncher ${D}${bindir}
	cd ${S}/WebKitBuilds/Debug
	PWD=`pwd` ${WEBKIT_QMAKE} WEBKIT_INC_DIR=${D}${prefix}/include WEBKIT_LIB_DIR=${D}${libdir} $PWD/../../WebKit.pro
	oe_runmake install
}

