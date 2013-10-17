require mesa_${PV}.bb

SUMMARY += " (OpenGL only, no EGL/GLES)"

FILESPATH = "${@base_set_filespath(["${FILE_DIRNAME}/mesa-${PV}", "${FILE_DIRNAME}/mesa"], d)}"

PROVIDES = "virtual/libgl virtual/mesa"

PACKAGECONFIG ??= "dri ${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"

EXCLUDE_FROM_WORLD = "1"
