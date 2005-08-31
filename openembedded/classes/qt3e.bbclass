#
# override variables set by qmake-base to compile Qt/X11 apps
#
export QTDIR="${STAGING_DIR}/${HOST_SYS}/qte3"
export QTEDIR="${STAGING_DIR}/${HOST_SYS}/qte3"
export OE_QMAKE_UIC="${STAGING_BINDIR}/uic3"
export OE_QMAKE_MOC="${STAGING_BINDIR}/moc3"
export OE_QMAKE_CXXFLAGS="${CXXFLAGS} "
export OE_QMAKE_INCDIR_QT="${QTEDIR}/include"
export OE_QMAKE_LIBDIR_QT="${QTEDIR}/lib"
export OE_QMAKE_LIBS_QT="qte"
