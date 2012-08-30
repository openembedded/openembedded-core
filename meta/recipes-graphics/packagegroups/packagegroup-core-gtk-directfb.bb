DESCRIPTION = "gkt+ over directfb without x11"
PR = "r0"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit packagegroup

TOUCH = ' ${@base_contains("MACHINE_FEATURES", "touchscreen", "tslib tslib-calibrate tslib-tests", "",d)}'

RDEPENDS_${PN} = " \
		directfb \
		directfb-examples \
		pango \
		pango-modules \
		fontconfig \
		gtk+ \
		gtk-demo \
		${TOUCH} \
"
