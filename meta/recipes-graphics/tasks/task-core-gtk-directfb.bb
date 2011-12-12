DESCRIPTION = "gkt+ over directfb without x11"
PR = "r0"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit task

TOUCH = ' ${@base_contains("MACHINE_FEATURES", "touchscreen", "tslib tslib-calibrate tslib-tests", "",d)}'

PACKAGES += " \
	${PN}-base \
"

RDEPENDS_${PN}-base = " \
		directfb \
		directfb-examples \
		pango \
		pango-modules \
		fontconfig \
		gtk+ \
		gtk-demo \
		dropbear \
		${TOUCH} \
"
