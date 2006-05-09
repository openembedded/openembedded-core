DEPENDS_prepend = "qmake2-native "
DEPENDS_prepend = "${@["qt4x11 ", ""][(bb.data.getVar('PN', d, 1) == 'qt4-x11-free')]}"
#
# override variables set by qmake-base to compile Qt4/X11 apps
#
export QTDIR = "${STAGING_DIR}/${HOST_SYS}/qt4"
export QMAKESPEC = "${QTDIR}/mkspecs/${TARGET_OS}-oe-g++"
export OE_QMAKE_UIC = "${STAGING_BINDIR}/uic4"
export OE_QMAKE_MOC = "${STAGING_BINDIR}/moc4"
export OE_QMAKE_RCC = "${STAGING_BINDIR}/rcc4"
export OE_QMAKE_QMAKE = "${STAGING_BINDIR}/qmake2"
export OE_QMAKE_LINK = "${CXX}"
export OE_QMAKE_CXXFLAGS = "${CXXFLAGS}"
export OE_QMAKE_INCDIR_QT = "${QTDIR}/include"
export OE_QMAKE_LIBDIR_QT = "${QTDIR}/lib"
export OE_QMAKE_LIBS_QT = "qt"
export OE_QMAKE_LIBS_X11 = "-lXext -lX11 -lm"
