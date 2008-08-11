include mesa-common.inc

PACKAGES =+ "libglu libglu-dev libosmesa libosmesa-dev libgl libgl-dev"

FILES_libgl = "${libdir}/libGL.so.*"
FILES_libglu = "${libdir}/libGLU.so.*"
FILES_libosmesa = "${libdir}/libOSMesa.so.*"

FILES_libgl-dev = "${libdir}/libGL.* ${includedir}/GL"
FILES_libglu-dev = "${libdir}/libGLU.* ${includedir}/GL/glu*.h"
FILES_libosmesa-dev = "${libdir}/libOSMesa.* ${includedir}/osmesa.h"

do_configure() {
	cd configs

	cp linux current
	sed -e "s%CC *= *.*%CC = ${CC}%" -i current
	sed -e "s%CXX *= *.*%CXX = ${CXX}%" -i current
	sed -e "s%LD *= *.*%LD = ${LD}%" -i current
	sed -e "s%OPT_FLAGS *= *.*%OPT_FLAGS = ${TARGET_CFLAGS}%" -i current
	sed -e "s%X11_INCLUDES *= *.*%X11_INCLUDES = -I${STAGING_INCDIR}/X11%" -i current
	sed -e "s%EXTRA_LIB_PATH *= *.*%EXTRA_LIB_PATH = ${LDFLAGS}%" -i current
	sed -i s:\$\(CC\):gcc:g  ../src/mesa/x86/Makefile
	echo "SRC_DIRS = mesa glu glut/glx" >> current
}

do_compile() {
	oe_runmake default
}

do_install() {
	install -d ${D}${libdir}
	cp -pP lib/* ${D}${libdir}/
	install -d ${D}${includedir}
	cp -R include/GL ${D}${includedir}/
}

do_stage() {
        cp -pP lib/* ${STAGING_LIBDIR}/
        cp -R include/GL ${STAGING_INCDIR}/
}
