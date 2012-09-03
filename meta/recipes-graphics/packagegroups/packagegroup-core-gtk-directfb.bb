SUMMARY = "GTK+ over DirectFB without X11"
PR = "r0"
LICENSE = "MIT"

inherit packagegroup

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-gtk-directfb-base"

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
