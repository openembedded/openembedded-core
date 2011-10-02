require qt4.inc

DESCRIPTION = "Qt is a versatile cross-platform application framework -- this is the X11 version."
HOMEPAGE = "http://qt.nokia.com"
SECTION = "x11/libs"
DEPENDS += "virtual/libgl virtual/libx11 fontconfig libxft libxext libxrender libxrandr libxcursor"

INC_PR = "r30"

QT_GLFLAGS ?= "${@base_contains('DISTRO_FEATURES', 'opengl', '-opengl', '-no-opengl', d)} "
QT_GLFLAGS_qemux86 = "-opengl"
QT_GLFLAGS_qemuppc = "-opengl"
QT_CONFIG_FLAGS += "-no-xinerama -no-xkb ${QT_GLFLAGS}"
QT_BASE_NAME ?= "qt4"
QT_BASE_LIB  ?= "libqt"

inherit qt4x11

