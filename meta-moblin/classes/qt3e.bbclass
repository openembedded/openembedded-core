#
# override variables set by qmake_base to compile Qt/X11 apps
#
export QTDIR="${STAGING_DIR_HOST}/qte3"
export QTEDIR="${STAGING_DIR_HOST}/qte3"
export OE_QMAKE_UIC="${STAGING_BINDIR_NATIVE}/uic3"
export OE_QMAKE_MOC="${STAGING_BINDIR_NATIVE}/moc3"
export OE_QMAKE_CXXFLAGS="${CXXFLAGS} "
export OE_QMAKE_INCDIR_QT="${QTEDIR}/include"
export OE_QMAKE_LIBDIR_QT="${QTEDIR}/lib"
export OE_QMAKE_LIBS_QT="qte"
