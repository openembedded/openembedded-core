# Qt Embedded toolchain
PR = "r5"
TOOLCHAIN_HOST_TASK = "task-qte-toolchain-host-nativesdk task-cross-canadian-${TRANSLATED_TARGET_ARCH}"
TOOLCHAIN_TARGET_TASK = "task-qte-toolchain-target"
TOOLCHAIN_OUTPUTNAME = "${SDK_NAME}-toolchain-qte-${DISTRO_VERSION}"

require recipes-core/meta/meta-toolchain.bb

QT_DIR_NAME = "qtopia"
QT_TOOLS_PREFIX = "${SDKPATHNATIVE}${bindir_nativesdk}"

toolchain_create_sdk_env_script_append() {
    echo 'export OE_QMAKE_CFLAGS="$CFLAGS"' >> $script
    echo 'export OE_QMAKE_CXXFLAGS="$CXXFLAGS"' >> $script
    echo 'export OE_QMAKE_LDFLAGS="$LDFLAGS"' >> $script
    echo 'export OE_QMAKE_CC=${TARGET_PREFIX}gcc' >> $script
    echo 'export OE_QMAKE_CXX=${TARGET_PREFIX}g++' >> $script
    echo 'export OE_QMAKE_LINK=${TARGET_PREFIX}g++' >> $script
    echo 'export OE_QMAKE_AR=${TARGET_PREFIX}ar' >> $script
    echo 'export OE_QMAKE_LIBDIR_QT=${SDKTARGETSYSROOT}/${libdir}' >> $script
    echo 'export OE_QMAKE_INCDIR_QT=${SDKTARGETSYSROOT}/${includedir}/${QT_DIR_NAME}' >> $script
    echo 'export OE_QMAKE_MOC=${QT_TOOLS_PREFIX}/moc4' >> $script
    echo 'export OE_QMAKE_UIC=${QT_TOOLS_PREFIX}/uic4' >> $script
    echo 'export OE_QMAKE_UIC3=${QT_TOOLS_PREFIX}/uic34' >> $script
    echo 'export OE_QMAKE_RCC=${QT_TOOLS_PREFIX}/rcc4' >> $script
    echo 'export OE_QMAKE_QDBUSCPP2XML=${QT_TOOLS_PREFIX}/qdbuscpp2xml4' >> $script
    echo 'export OE_QMAKE_QDBUSXML2CPP=${QT_TOOLS_PREFIX}/qdbusxml2cpp4' >> $script
    echo 'export OE_QMAKE_QT_CONFIG=${SDKTARGETSYSROOT}/${datadir}/${QT_DIR_NAME}/mkspecs/qconfig.pri' >> $script
    echo 'export QMAKESPEC=${SDKTARGETSYSROOT}/${datadir}/${QT_DIR_NAME}/mkspecs/linux-g++' >> $script

    # make a symbolic link to mkspecs for compatibility with Nokia's SDK
    # and QTCreator
    (cd ${SDK_OUTPUT}/${QT_TOOLS_PREFIX}/..; ln -s ${SDKTARGETSYSROOT}/usr/share/qtopia/mkspecs mkspecs;)
}
