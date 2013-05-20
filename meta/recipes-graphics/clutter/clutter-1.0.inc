DESCRIPTION = "Clutter graphics library"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPLv2.1+"

inherit clutter

SRC_URI += "file://enable_tests.patch"

DEPENDS = "gtk-doc-native pango glib-2.0 json-glib atk udev cogl-1.0"
PACKAGES =+ "${PN}-examples"
AUTOTOOLS_AUXDIR = "${S}/build"

EDEPENDS_X11 = "virtual/libx11 libxi libxfixes"
EDEPENDS_GLX = "virtual/libgl"
EDEPENDS_EGL = "virtual/egl"

EDEPENDS_EVDEV = "libxkbcommon"
ERDEPENDS_EVDEV = "xkeyboard-config"

# Disable pretty much everything, override in platform specific set up
EXTRA_OECONF += "--disable-gtk-doc		\
	     	 ${@get_fpu_setting(bb, d)}	\
             	 --disable-introspection	\
	     	 --disable-egl-backend		\
	     	 --disable-quartz-backend	\
	     	 --disable-win32-backend	\
	     	 --disable-x11-backend		\
	     	 --disable-gdk-backend		\
	     	 --disable-wayland-backend	\
	     	 --disable-wayland-compositor	\
	     	 --disable-cex100-backend	\
	     	 --disable-tslib-input		\
	     	 --disable-evdev-input		\
	    	"

PACKAGECONFIG[x11] = "--enable-x11-backend,,${EDEPENDS_X11}"
PACKAGECONFIG[glx] = "--enable-x11-backend,,${EDEPENDS_X11} ${EDEPENDS_GLX}"
PACKAGECONFIG[egl] = "--enable-egl-backend,,${EDEPENDS_EGL}"
PACKAGECONFIG[evdev] = "--enable-evdev-input,,${EDEPENDS_EVDEV},${ERDEPENDS_EVDEV}"

# Default configuration, distros might want to override
PACKAGECONFIG ??= "glx"

FILES_${PN}-examples = "${bindir}/test-* ${pkgdatadir}/redhand.png"

do_configure_prepend() {
        # Fix up some weirdness in the docs
        sed -i s:doc/reference/Makefile::g ${S}/configure.ac
        sed -i s:doc::g ${S}/Makefile.am

	# see https://bugzilla.gnome.org/show_bug.cgi?id=661128 for this
	touch -t 200001010000 po/clutter-1.0.pot

	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}
