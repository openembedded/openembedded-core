DEPENDS_prepend = "${@["qt4-x11-free ", ""][(d.getVar('BPN', True)[:12] == 'qt4-x11-free')]}"

inherit qmake2

QT_BASE_NAME = "qt4"
QT_DIR_NAME = "qt4"
QT_LIBINFIX = ""

# Qt4 uses atomic instructions not supported in thumb mode
ARM_INSTRUCTION_SET = "arm"
