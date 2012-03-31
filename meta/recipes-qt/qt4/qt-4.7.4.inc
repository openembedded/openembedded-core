LICENSE = "LGPLv2.1 | GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=fbc093901857fcd118f065f900982c24 \
                    file://LICENSE.GPL3;md5=babc5b6b77441da277f5c06b2e547720 \
                    file://LGPL_EXCEPTION.txt;md5=411080a56ff917a5a1aa08c98acae354"

FILESPATH =. "${FILE_DIRNAME}/qt-${PV}:"

SRC_URI = "http://get.qt.nokia.com/qt/source/qt-everywhere-opensource-src-${PV}.tar.gz \
           file://0004-no-qmake.patch \
           file://hack-out-pg2-4.7.0.patch \
           file://0006-freetype-host-includes.patch \
           file://0008-qt-lib-infix.patch \
           file://0009-support-2bpp.patch \
           file://0001-Added-Openembedded-crossarch-option.patch \
           file://blacklist-diginotar-certs.diff \
           file://fix-translations.patch \
           file://add_nostrip_for_debug_packages.diff \
           file://target_qmake.patch \
           file://pulseaudio-config.patch \
           file://g++.conf \
           file://linux.conf \
           file://fix-qtbug-20925.patch \
	   file://gcc47-fix.patch \
           "

SRC_URI[md5sum] = "9831cf1dfa8d0689a06c2c54c5c65aaf"
SRC_URI[sha256sum] = "97195ebce8a46f9929fb971d9ae58326d011c4d54425389e6e936514f540221e"

S = "${WORKDIR}/qt-everywhere-opensource-src-${PV}"

FILES_${QT_BASE_NAME}-tools                += "${bindir}/qml"

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
}

