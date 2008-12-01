DEPENDS_prepend = "${@["qt3x11 ", ""][(bb.data.getVar('PN', d, 1) == 'qt-x11-free')]}"
EXTRA_QMAKEVARS_POST += "CONFIG+=thread"
#
# override variables set by qmake_base to compile Qt/X11 apps
#
export QTDIR = "${STAGING_DIR_HOST}/qt3"
export OE_QMAKE_UIC = "${STAGING_BINDIR_NATIVE}/uic3"
export OE_QMAKE_MOC = "${STAGING_BINDIR_NATIVE}/moc3"
export OE_QMAKE_CXXFLAGS = "${CXXFLAGS} -DQT_NO_XIM"
export OE_QMAKE_INCDIR_QT = "${QTDIR}/include"
export OE_QMAKE_LIBDIR_QT = "${QTDIR}/lib"
export OE_QMAKE_LIBS_QT = "qt"
export OE_QMAKE_LIBS_X11 = "-lXext -lX11 -lm"
export OE_QMAKE_LIBS_OPENGL = "-lGLU -lGL -lXmu"
export OE_QMAKE_LIBS_OPENGL_QT = "-lGL -lXmu"
