# Qt Embedded toolchain
PR = "r5"
TOOLCHAIN_HOST_TASK = "task-qte-toolchain-host"
TOOLCHAIN_TARGET_TASK = "task-qte-toolchain-target"

require recipes-core/meta/meta-toolchain.bb
SDK_SUFFIX = "toolchain-qte"

QT_DIR_NAME = "qtopia"

toolchain_create_sdk_env_script_append() {
    echo 'export OE_QMAKE_CC=${TARGET_SYS}-gcc' >> $script
    echo 'export OE_QMAKE_CXX=${TARGET_SYS}-g++' >> $script
    echo 'export OE_QMAKE_LINK=${TARGET_SYS}-g++' >> $script
    echo 'export OE_QMAKE_AR=${TARGET_SYS}-ar' >> $script
    echo 'export OE_QMAKE_LIBDIR_QT=${SDKPATH}/${TARGET_SYS}/${libdir}' >> $script
    echo 'export OE_QMAKE_INCDIR_QT=${SDKPATH}/${TARGET_SYS}/${includedir}/${QT_DIR_NAME}' >> $script
    echo 'export OE_QMAKE_MOC=${SDKPATH}/bin/moc4' >> $script
    echo 'export OE_QMAKE_UIC=${SDKPATH}/bin/uic4' >> $script
    echo 'export OE_QMAKE_UIC3=${SDKPATH}/bin/uic34' >> $script
    echo 'export OE_QMAKE_RCC=${SDKPATH}/bin/rcc4' >> $script
    echo 'export OE_QMAKE_QDBUSCPP2XML=${SDKPATH}/bin/qdbuscpp2xml4' >> $script
    echo 'export OE_QMAKE_QDBUSXML2CPP=${SDKPATH}/bin/qdbusxml2cpp4' >> $script
    echo 'export OE_QMAKE_QT_CONFIG=${SDKPATH}/${TARGET_SYS}/${datadir}/${QT_DIR_NAME}/mkspecs/qconfig.pri' >> $script
    echo 'export QMAKESPEC=${SDKPATH}/${TARGET_SYS}/${datadir}/${QT_DIR_NAME}/mkspecs/linux-g++' >> $script
}
