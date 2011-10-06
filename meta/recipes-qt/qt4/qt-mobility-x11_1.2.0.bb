DESCRIPTION = "Qt Mobility 1.2.0 - version for Qt/X11"
DEPENDS = "qt4-x11-free"
SECTION = "x11/libs"
qtm_embedded := ""
qtm_dir = "qt4"
qtm_glflags := "${@base_contains('DISTRO_FEATURES', 'opengl', '+=opengl', '-=opengl', d)} "
qtm_extra_config := ""

inherit qt4x11
require qt-mobility_${PV}.inc
