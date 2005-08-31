DEPENDS_prepend = "qmake-native "

OE_QMAKE_PLATFORM = "${TARGET_OS}-oe-g++"
QMAKESPEC := "${QMAKE_MKSPEC_PATH}/${OE_QMAKE_PLATFORM}"

# We override this completely to eliminate the -e normally passed in
EXTRA_OEMAKE = ' MAKEFLAGS= '

export OE_QMAKE_CC="${CC}"
export OE_QMAKE_CFLAGS="${CFLAGS}"
export OE_QMAKE_CXX="${CXX}"
export OE_QMAKE_CXXFLAGS="-fno-exceptions -fno-rtti ${CXXFLAGS}"
export OE_QMAKE_LDFLAGS="${LDFLAGS}"
export OE_QMAKE_LINK="${CCLD}"
export OE_QMAKE_AR="${AR}"
export OE_QMAKE_STRIP="${STRIP}"
export OE_QMAKE_UIC="${STAGING_BINDIR}/uic"
export OE_QMAKE_MOC="${STAGING_BINDIR}/moc"
export OE_QMAKE_RPATH="-Wl,-rpath-link,"

# default to qte2 via bb.conf, inherit qt3x11 to configure for qt3x11
export OE_QMAKE_INCDIR_QT="${QTDIR}/include"
export OE_QMAKE_LIBDIR_QT="${QTDIR}/lib"
export OE_QMAKE_LIBS_QT="qte"
export OE_QMAKE_LIBS_X11=""

oe_qmake_mkspecs () {
    mkdir -p mkspecs/${OE_QMAKE_PLATFORM}
    for f in ${QMAKE_MKSPEC_PATH}/${OE_QMAKE_PLATFORM}/*; do
        if [ -L $f ]; then
            lnk=`readlink $f`
            if [ -f mkspecs/${OE_QMAKE_PLATFORM}/$lnk ]; then
                ln -s $lnk mkspecs/${OE_QMAKE_PLATFORM}/`basename $f`
            else
                cp $f mkspecs/${OE_QMAKE_PLATFORM}/
            fi
        else
            cp $f mkspecs/${OE_QMAKE_PLATFORM}/
        fi
    done
}

