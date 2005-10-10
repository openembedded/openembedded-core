#
# override variables set by qmake-base to compile Qt/X11 apps
#
export QTDIR="${STAGING_DIR}/${HOST_SYS}/qt4"
export OE_QMAKE_UIC="${STAGING_BINDIR}/uic4"
export OE_QMAKE_MOC="${STAGING_BINDIR}/moc4"
export OE_QMAKE_CXXFLAGS="${CXXFLAGS}"
export OE_QMAKE_INCDIR_QT="${QTDIR}/include"
export OE_QMAKE_LIBDIR_QT="${QTDIR}/lib"
export OE_QMAKE_LIBS_QT="qt"
export OE_QMAKE_LIBS_X11="-lXext -lX11 -lm"
