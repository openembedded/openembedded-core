LICENSE = "LGPLv2.1 | GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=fbc093901857fcd118f065f900982c24 \
                    file://LICENSE.GPL3;md5=babc5b6b77441da277f5c06b2e547720 \
                    file://LGPL_EXCEPTION.txt;md5=411080a56ff917a5a1aa08c98acae354"

FILESPATH =. "${FILE_DIRNAME}/qt-${PV}:"

SRC_URI = "http://get.qt.nokia.com/qt/source/qt-everywhere-opensource-src-${PV}.tar.gz \
           file://0004-no-qmake.patch \
           file://hack-out-pg2-4.7.0.patch \
           file://0006-freetype-host-includes.patch \
           file://0009-support-2bpp.patch \
           file://0001-Added-Openembedded-crossarch-option.patch \
           file://fix-translations.patch \
           file://add_nostrip_for_debug_packages.diff \
           file://qmake_cxx_eval.patch \
           file://configure_oe_compiler.patch \
           file://target_qmake.patch \
           file://qmake_pri_fixes.patch \
           file://pulseaudio-config.patch \
           file://g++.conf \
           file://linux.conf \
           "

SRC_URI[md5sum] = "7960ba8e18ca31f0c6e4895a312f92ff"
SRC_URI[sha256sum] = "ef851a36aa41b4ad7a3e4c96ca27eaed2a629a6d2fa06c20f072118caed87ae8"

S = "${WORKDIR}/qt-everywhere-opensource-src-${PV}"

FILES_${QT_BASE_NAME}-tools_append     = " ${bindir}/qml ${bindir}/qmlplugindump"
FILES_${QT_BASE_NAME}-tools-dbg_append = " ${bindir}/.debug/qml ${bindir}/.debug/qmlplugindump"

PACKAGES_append = " ${QT_BASE_NAME}-tests-dbg ${QT_BASE_NAME}-tests"
FILES_${QT_BASE_NAME}-tests-dbg        = "/usr/tests/qt4/*/.debug"
FILES_${QT_BASE_NAME}-tests            = "/usr/tests/qt4/*"

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

