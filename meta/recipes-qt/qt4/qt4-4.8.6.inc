LICENSE = "LGPLv2.1 | GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=fbc093901857fcd118f065f900982c24 \
                    file://LICENSE.GPL3;md5=6e1694ee338db410417517884918d4d2 \
                    file://LGPL_EXCEPTION.txt;md5=0145c4d1b6f96a661c2c139dfb268fb6"

FILESEXTRAPATHS =. "${FILE_DIRNAME}/qt4-${PV}:"

SRC_URI = "http://download.qt-project.org/official_releases/qt/4.8/${PV}/qt-everywhere-opensource-src-${PV}.tar.gz \
           file://0003-webkit2-set-OUTPUT_DIR-value-if-empty.patch \
           file://0004-configure-qmake-is-already-built-in-qt4-tools-native.patch \
           file://0008-qmake.pro-Allow-building-a-separate-qmake-for-the-ta.patch \
           file://0009-qmake-fix-source-file-references-in-qmake.pri.patch \
           file://0010-configure-Hack-to-not-use-the-pg_config-of-the-host-.patch \
           file://0011-freetype-host-includes.patch \
           file://0012-Add-2bpp-support.patch \
           file://0013-configure-add-crossarch-option.patch \
           file://0014-translations-fix-phony-translation-linking-error.patch \
           file://0015-configure-add-nostrip-for-debug-packages.patch \
           file://0016-configure-eval-QMAKE_CXX.patch \
           file://0017-configure-ensure-we-identify-the-compiler-as-g-in-co.patch \
           file://0018-configure-make-pulseaudio-a-configurable-option.patch \
           file://0019-Fixes-for-gcc-4.7.0-particularly-on-qemux86.patch \
           file://0027-tools.pro-disable-qmeegographicssystemhelper.patch \
           file://0028-Don-t-crash-on-broken-GIF-images.patch \
           file://0034-Fix-a-division-by-zero-when-processing-malformed-BMP.patch \
           file://g++.conf \
           file://linux.conf \
           "

SRC_URI[md5sum] = "2edbe4d6c2eff33ef91732602f3518eb"
SRC_URI[sha256sum] = "8b14dd91b52862e09b8e6a963507b74bc2580787d171feda197badfa7034032c"

S = "${WORKDIR}/qt-everywhere-opensource-src-${PV}"

FILES_${QT_BASE_NAME}-tools_append     = " ${bindir}/qml ${bindir}/qmlplugindump"
FILES_${QT_BASE_NAME}-tools-dbg_append = " ${bindir}/.debug/qml ${bindir}/.debug/qmlplugindump"

PACKAGES_append = " ${QT_BASE_NAME}-tests-dbg ${QT_BASE_NAME}-tests"
FILES_${QT_BASE_NAME}-tests-dbg        = "${prefix}/tests/qt4/*/.debug"
FILES_${QT_BASE_NAME}-tests            = "${prefix}/tests/qt4/*"

do_configure_prepend() {
    for pro in $(find ${S} -name "*.pro") ; do
        sed -i \
            -e 's:$$QT_BUILD_TREE/bin/lrelease:${OE_QMAKE_LRELEASE}:g' \
            -e 's:qtPrepareTool(LRELEASE, lrelease):LRELEASE = ${OE_QMAKE_LRELEASE}:g' $pro
    done

    sed -i s:SEDME:${S}: ${WORKDIR}/linux.conf
    sed -i \
    -e /QMAKE_MOC\ /d \
    -e /QMAKE_UIC\ /d \
    -e /QMAKE_UIC3\ /d \
    -e /QMAKE_RCC\ /d \
    ${S}/configure

    # Avoid problems with Qt 4.8.0 configure setting QMAKE_LINK from LD (since we want the linker to be g++)
    unset LD
}

