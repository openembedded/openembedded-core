require mesa.inc

SUMMARY += " (OpenGL only, no EGL/GLES)"

PROVIDES = "virtual/libgl virtual/mesa"

S = "${UNPACKDIR}/mesa-${PV}"

TARGET_CFLAGS = "-I${STAGING_INCDIR}/drm"

# At least one DRI rendering engine is required to build mesa.
PACKAGECONFIG = "opengl gallium ${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"
