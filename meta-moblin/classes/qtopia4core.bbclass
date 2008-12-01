DEPENDS_prepend = "${@["qtopia-core ", ""][(bb.data.getVar('PN', d, 1) == 'qtopia-core')]}"
inherit qmake2

QT_DIR_NAME = "qtopia"
#
# override variables set by qmake-base to compile QtopiaCore apps
#
export OE_QMAKE_INCDIR_QT = "${STAGING_INCDIR}/qtopia"
export OE_QMAKE_LIBDIR_QT = "${STAGING_LIBDIR}"
export OE_QMAKE_LIBS_QT = "qt"
export OE_QMAKE_LIBS_X11 = ""
export OE_QMAKE_EXTRA_MODULES = "network"
EXTRA_QMAKEVARS_PRE += " QT_LIBINFIX=E "
