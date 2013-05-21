DESCRIPTION = "a modern 3D graphics API with associated utility APIs"
HOMEPAGE = "http://wiki.clutter-project.org/wiki/Cogl"
LICENSE = "LGPLv2.1+"

inherit clutter

DEPENDS = "pango glib-2.0 gdk-pixbuf"
PACKAGES =+ "${PN}-examples"
AUTOTOOLS_AUXDIR = "${S}/build"

# Extra DEPENDS for PACKAGECONFIG
EDEPENDS_GL    = "virtual/libgl libdrm"
EDEPENDS_GLES2 = "virtual/libgles2"
EDEPENDS_KMS   = "libdrm virtual/egl"
EDEPENDS_EGL   = "virtual/egl"
EDEPENDS_X11   = "virtual/libx11 libxcomposite libxfixes libxi"

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

# egl backends
PACKAGECONFIG[egl-kms] = "--enable-kms-egl-platform,--disable-kms-egl-platform,${EDEPENDS_KMS}"
PACKAGECONFIG[egl-null] = "--enable-null-egl-platform,--disable-null-egl-platform"
PACKAGECONFIG[egl-x11] = "--enable-xlib-egl-platform,--disable-xlib-egl-platform,${EDEPENDS_X11}"

# glx
PACKAGECONFIG[glx] = "--enable-glx,--disable-glx,${EDEPENDS_GL} ${EDEPENDS_X11}"

# Default to GLX
PACKAGECONFIG ??= "gl glx"

#Fix up some weirdness in the docs
do_configure_prepend() {
        sed -i s:doc/reference/Makefile::g ${S}/configure.ac
        sed -i s:doc::g ${S}/Makefile.am
}

FILES_${PN}-examples = "${bindir}/* ${datadir}/cogl/examples-data/*"
