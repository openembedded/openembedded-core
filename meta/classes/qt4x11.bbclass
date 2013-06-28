QT4DEPENDS ?= "qt4-x11 "
DEPENDS_prepend = "${QT4DEPENDS}"

inherit qmake2

QT_BASE_NAME = "qt4"
QT_DIR_NAME = "qt4"
QT_LIBINFIX = ""

# Qt4 uses atomic instructions not supported in thumb mode
ARM_INSTRUCTION_SET = "arm"

# Qt4 could NOT be built on MIPS64 with 64 bits userspace
COMPATIBLE_HOST_mips64 = "mips64.*-linux-gnun32"
