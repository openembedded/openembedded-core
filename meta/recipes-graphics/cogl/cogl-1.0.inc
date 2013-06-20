DESCRIPTION = "a modern 3D graphics API with associated utility APIs"
HOMEPAGE = "http://wiki.clutter-project.org/wiki/Cogl"
LICENSE = "LGPLv2.1+"

SRC_URI += "file://cogl_fixed_mul-constraint.patch \
            file://cogl-fixed-thumb.patch \
            "

inherit clutter

DEPENDS = "pango glib-2.0 gdk-pixbuf"
PACKAGES =+ "${PN}-examples"
AUTOTOOLS_AUXDIR = "${S}/build"

# Extra DEPENDS for PACKAGECONFIG
EDEPENDS_GL = "virtual/libgl libdrm"
EDEPENDS_GLES2 = "virtual/libgles2"
EDEPENDS_KMS = "libdrm virtual/egl"
EDEPENDS_EGL = "virtual/egl"
EDEPENDS_X11 = "virtual/libx11 libxcomposite libxfixes libxi libxrandr"
EDEPENDS_WAYLAND = "wayland"

# Extra RDEPENDS for PACKAGECONFIG
# This has to be explictly listed, because cogl dlopens the backends
ERDEPENDS_GL    = "libgl"
ERDEPENDS_GLES2 = "libgles2"

EXTRA_OECONF += "--disable-introspection	\
	       	 --enable-examples-install	\
	       	 --enable-debug		 	\
	       	 --disable-gles1		\
	    	"

# GL flavours
PACKAGECONFIG[gl] = "--enable-gl,--disable-gl,${EDEPENDS_GL},${ERDEPENDS_GL}"
PACKAGECONFIG[gles2] = "--enable-gles2,--disable-gles2,${EDEPENDS_GLES2}, ${ERDEPENDS_GLES2}"

# EGL backends
PACKAGECONFIG[egl-kms] = "--enable-kms-egl-platform,--disable-kms-egl-platform,${EDEPENDS_KMS}"
PACKAGECONFIG[egl-null] = "--enable-null-egl-platform,--disable-null-egl-platform"
PACKAGECONFIG[egl-x11] = "--enable-xlib-egl-platform,--disable-xlib-egl-platform,${EDEPENDS_X11}"
PACKAGECONFIG[egl-wayland] = "--enable-wayland-egl-platform,--disable-wayland-egl-platform,${EDEPENDS_WAYLAND}"

# GLX
PACKAGECONFIG[glx] = "--enable-glx,--disable-glx,${EDEPENDS_GL} ${EDEPENDS_X11}"

# Wayland server-side
PACKAGECONFIG[wayland-server] = "--enable-wayland-egl-server,--disable-wayland-egl-server,${EDEPENDS_WAYLAND}"

# Respect the DISTRO_FEATURES to pull in GLX or Wayland as appropriate by
# default.
PACKAGECONFIG ??= "gl \
                   ${@base_contains('DISTRO_FEATURES', 'wayland', 'egl-wayland', '', d)} \
                   ${@base_contains('DISTRO_FEATURES', 'x11', 'glx', '', d)}"

FILES_${PN}-examples = "${bindir}/* ${datadir}/cogl/examples-data/*"
