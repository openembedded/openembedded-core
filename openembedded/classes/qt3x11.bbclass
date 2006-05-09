DEPENDS_prepend = "${@["qt3x11 ", ""][(bb.data.getVar('PN', d, 1) == 'qt-x11-free')]}"
EXTRA_QMAKEVARS_POST += "CONFIG+=thread"
#
# override variables set by qmake-base to compile Qt/X11 apps
#
export QTDIR = "${STAGING_DIR}/${HOST_SYS}/qt3"
export OE_QMAKE_UIC = "${STAGING_BINDIR}/uic3"
export OE_QMAKE_MOC = "${STAGING_BINDIR}/moc3"
export OE_QMAKE_CXXFLAGS = "${CXXFLAGS} -DQT_NO_XIM"
export OE_QMAKE_INCDIR_QT = "${QTDIR}/include"
export OE_QMAKE_LIBDIR_QT = "${QTDIR}/lib"
export OE_QMAKE_LIBS_QT = "qt"
export OE_QMAKE_LIBS_X11 = "-lXext -lX11 -lm"


