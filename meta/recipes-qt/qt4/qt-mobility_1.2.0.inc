HOMEPAGE = "http://qt.nokia.com"
DEPENDS += "pulseaudio bluez4"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=fbc093901857fcd118f065f900982c24 \
		    file://LGPL_EXCEPTION.txt;md5=411080a56ff917a5a1aa08c98acae354"

SRC_URI = "http://get.qt.nokia.com/qt/add-ons/qt-mobility-opensource-src-${PV}.tar.gz \
           file://qt-mobility-configure.patch \
           file://0001-gstvideoconnector-fixed-buffers-allocation.patch \
           file://0002-Remove-unnecessary-rpaths-from-qml_device-example.patch \
           file://qt-mobility-no-opengl.patch \
	   file://gcc-scope.patch \
	   "

SRC_URI[md5sum]="ea5db5a8d3dd4709c2926dceda646bd8"
SRC_URI[sha256sum]="ee3c88975e04139ac9589f76d4be646d44fcbc4c8c1cf2db621abc154cf0ba44"

PR = "r5"

S = "${WORKDIR}/qt-mobility-opensource-src-${PV}"

qtm_bin		:= "/usr/bin/${qtm_dir}"
qtm_lib		:= "/usr/lib"
qtm_include	:= "/usr/include/${qtm_dir}"
qtm_examples	:= "/usr/bin/${qtm_dir}/examples/QtMobility"
qtm_demos	:= "/usr/bin/${qtm_dir}/demos/QtMobility"
qtm_plugins	:= "/usr/lib/${qtm_dir}/plugins/QtMobility"
qtm_data	:= "/usr/share/${qtm_dir}"
qtm_imports	:= "/usr/lib/${qtm_dir}/imports"

do_configure_prepend() {
	cp qtmobility.pro qtmobility.pro.old
	sed -e 's@...QT_INSTALL_DATA.@${qtm_data}@' <qtmobility.pro.old >qtmobility.pro
	for NAME in plugins/declarative/*/*.pro
	do
	    cp $NAME $NAME.old
	    sed -e 's@...QT_INSTALL_IMPORTS.@${qtm_imports}@' <$NAME.old >$NAME
	done
	cp staticconfig.pri staticconfig.pri.old
	echo "include(${STAGING_DATADIR}/${qtm_dir}/mkspecs/qconfig.pri)" >staticconfig.pri
	cat staticconfig.pri.old >>staticconfig.pri
	./configure -qmake-exec qmake2 -prefix /usr -examples -demos
	echo QT_MOBILITY_BIN = ${qtm_bin} >>./config.pri
	echo QT_MOBILITY_LIB = ${qtm_lib} >>./config.pri
	echo QT_MOBILITY_INCLUDE = ${qtm_include} >>./config.pri
	echo QT_MOBILITY_PLUGINS = ${qtm_plugins} >>./config.pri
	echo QT_MOBILITY_EXAMPLES = ${qtm_examples} >>./config.pri
	echo QT_MOBILITY_DEMOS = ${qtm_demos} >>./config.pri
	echo QT_CONFIG ${qtm_glflags} >>./config.pri
	echo bluez_enabled = yes >>./config.pri
	echo ${qtm_extra_config} >>./config.pri
	echo CONFIG += ${qtm_embedded} >>./config.pri
}

do_install() {
	for NAME in */*.prf
	do
	    cp $NAME $NAME.old
	    sed -e 's@/usr@${STAGING_BINDIR}/..@' <$NAME.old >$NAME
	done
	oe_runmake install INSTALL_ROOT=${D}
}

PACKAGES = "${PN}-dbg ${PN} ${PN}-dev"

FILES_${PN}-dbg = "${qtm_bin}/.debug ${qtm_examples}/.debug ${qtm_demos}/.debug \
                   ${qtm_lib}/.debug ${qtm_plugins}/*/.debug  ${qtm_imports}/*/.debug \
                   ${qtm_imports}/*/*/.debug ${bindir}/.debug /usr/src/debug"
FILES_${PN} = "${qtm_bin} ${qtm_examples} ${qtm_demos} ${qtm_lib}/*.so.1* ${qtm_plugins} ${qtm_imports}"
FILES_${PN}-dev = "${exec_prefix}"
