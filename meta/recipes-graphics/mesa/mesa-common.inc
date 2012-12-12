SUMMARY = "A free implementation of the OpenGL API"
DESCRIPTION = "Mesa is an open-source implementation of the OpenGL specification - \
a system for rendering interactive 3D graphics.  \
A variety of device drivers allows Mesa to be used in many different environments \
ranging from software emulation to complete hardware acceleration for modern GPUs. \
Mesa is used as part of the overall Direct Rendering Infrastructure and X.org \
environment."

HOMEPAGE = "http://mesa3d.org"
BUGTRACKER = "https://bugs.freedesktop.org"
SECTION = "x11"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://docs/license.html;md5=03ccdc4c379c4289aecfb8892c546f67"

INC_PR = "r8"
PE = "2"

DEPENDS = "libxml2-native makedepend-native flex-native bison-native"

PROVIDES = "virtual/libgl virtual/libgles1 virtual/libgles2 virtual/egl"

# for mesa-dri and mesa-xlib
FILESEXTRAPATHS_append := "${THISDIR}/mesa:"

inherit autotools pkgconfig pythonnative

EXTRA_OECONF = "--disable-glu \
                --disable-glw \
                --disable-glut \
                --enable-shared-glapi"

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} egl gles"

X11_DEPS = "xf86driproto glproto virtual/libx11 libxext libxxf86vm libxdamage libxfixes"
PACKAGECONFIG[x11] = "--enable-glx-tls,--disable-glx,${X11_DEPS}"

# Multiple virtual/gl providers being built breaks staging
EXCLUDE_FROM_WORLD = "1"

PACKAGES =+ "libegl-mesa libegl-mesa-dev \
             libosmesa libosmesa-dev \
             libgl-mesa libgl-mesa-dev \
             libdricore libdricore-dev \
             libglapi libglapi-dev \
             libgbm libgbm-dev \
             libgles1-mesa libgles1-mesa-dev \
             libgles2-mesa libgles2-mesa-dev \
             libgles3-mesa libgles3-mesa-dev \
            "

do_install_append () {
    # GLU is now in libglu (this shouldn't be required in Mesa 9.x)
    rm -f ${D}${includedir}/GL/glu*

    # Drivers never need libtool .la files
    rm -f ${D}${libdir}/dri/*.la
}

# For the packages that make up the OpenGL interfaces, inject variables so that
# they don't get Debian-renamed (which would remove the -mesa suffix), and
# RPROVIDEs/RCONFLICTs on the generic libgl name.
python __anonymous() {
    for p in (("libegl", "libegl1"), ("libgl", "libgl1"),
              ("libgles1", "libglesv1-cm1"), ("libgles2", "libglesv2-2"),
              ("libgles3",)):
        fullp = p[0] + "-mesa"
        pkgs = " ".join(p)
        d.setVar("DEBIAN_NOAUTONAME_" + fullp, "1")
        d.appendVar("RREPLACES_" + fullp, pkgs)
        d.appendVar("RPROVIDES_" + fullp, pkgs)
        d.appendVar("RCONFLICTS_" + fullp, pkgs)
        
        # For -dev, the first element is both the Debian and original name
        fullp += "-dev"
        pkgs = p[0] + "-dev"
        d.setVar("DEBIAN_NOAUTONAME_" + fullp, "1")
        d.appendVar("RREPLACES_" + fullp, pkgs)
        d.appendVar("RPROVIDES_" + fullp, pkgs)
        d.appendVar("RCONFLICTS_" + fullp, pkgs)
}

FILES_${PN} = "${sysconfdir}/drirc"
FILES_libdricore = "${libdir}/libdricore*.so.*"
FILES_libegl-mesa = "${libdir}/libEGL.so.*"
FILES_libgbm = "${libdir}/libgbm.so.*"
FILES_libgles1-mesa = "${libdir}/libGLESv1*.so.*"
FILES_libgles2-mesa = "${libdir}/libGLESv2.so.*"
FILES_libgl-mesa = "${libdir}/libGL.so.*"
FILES_libglapi = "${libdir}/libglapi.so.*"
FILES_libosmesa = "${libdir}/libOSMesa.so.*"

FILES_${PN}-dev = "${libdir}/pkgconfig/dri.pc"
FILES_libdricore-dev = "${libdir}/libdricore*.*"
FILES_libegl-mesa-dev = "${libdir}/libEGL.* ${includedir}/EGL ${includedir}/KHR ${libdir}/pkgconfig/egl.pc"
FILES_libgbm-dev = "${libdir}/libgbm.* ${libdir}/pkgconfig/gbm.pc ${includedir}/gbm.h"
FILES_libgl-mesa-dev = "${libdir}/libGL.* ${includedir}/GL ${libdir}/pkgconfig/gl.pc"
FILES_libglapi-dev = "${libdir}/libglapi.*"
FILES_libgles1-mesa-dev = "${libdir}/libGLESv1*.* ${includedir}/GLES ${libdir}/pkgconfig/glesv1*.pc"
FILES_libgles2-mesa-dev = "${libdir}/libGLESv2.* ${includedir}/GLES2 ${libdir}/pkgconfig/glesv2.pc"
FILES_libgles3-mesa-dev = "${includedir}/GLES3"
FILES_libosmesa-dev = "${libdir}/libOSMesa.* ${includedir}/osmesa.h ${libdir}/pkgconfig/osmesa.pc"

FILES_${PN}-dbg += "${libdir}/dri/.debug/* ${libdir}/egl/.debug/*"
