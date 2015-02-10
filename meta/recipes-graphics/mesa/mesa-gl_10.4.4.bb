require mesa_${PV}.bb

SUMMARY += " (OpenGL only, no EGL/GLES)"

FILESEXTRAPATHS =. "${FILE_DIRNAME}/mesa:"

PROVIDES = "virtual/libgl virtual/mesa"

PACKAGECONFIG ??= "dri ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"

EXCLUDE_FROM_WORLD = "1"
