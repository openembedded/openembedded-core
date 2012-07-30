DEPENDS_prepend = "${@base_contains("PROVIDES", "qt4-x11", "", "qt4-x11 ", d)}"

inherit qmake2

QT_BASE_NAME = "qt4"
QT_DIR_NAME = "qt4"
QT_LIBINFIX = ""

# Qt4 uses atomic instructions not supported in thumb mode
ARM_INSTRUCTION_SET = "arm"
