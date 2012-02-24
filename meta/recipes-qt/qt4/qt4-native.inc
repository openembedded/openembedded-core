DESCRIPTION = "Native version of Qt/[X11|Mac|Embedded]"
DEPENDS = "zlib-native dbus-native"
SECTION = "libs"
HOMEPAGE = "http://qt.nokia.com"
PROVIDES = "qt4-tools-native"

LICENSE = "LGPLv2.1 | GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL;md5=fbc093901857fcd118f065f900982c24 \
                    file://LICENSE.GPL3;md5=babc5b6b77441da277f5c06b2e547720 \
                    file://LGPL_EXCEPTION.txt;md5=411080a56ff917a5a1aa08c98acae354"

INC_PR = "r14"

inherit native

SRC_URI = "http://get.qt.nokia.com/qt/source/qt-everywhere-opensource-src-${PV}.tar.gz \
           file://qt-config.patch \
           file://g++.conf \
           file://linux.conf"
S = "${WORKDIR}/qt-everywhere-opensource-src-${PV}"

EXTRA_OECONF = "-prefix ${prefix} \
                -L ${STAGING_LIBDIR_NATIVE} \
                -I ${STAGING_INCDIR_NATIVE} \
                -qt-libjpeg -system-zlib \
                -no-libjpeg -no-libpng -no-libmng -no-libtiff \
                -no-accessibility \
                -no-cups \
                -no-nas-sound \
                -no-nis -no-openssl \
                -verbose -release \
                -headerdir ${includedir}/qt4 \
                -embedded -no-freetype -no-glib -no-iconv \
                -exceptions -xmlpatterns \
                -qt3support \
                -no-fast -silent -no-rpath"

# yank default -e, otherwise we get the following error:
# moc_qbuffer.cpp: No such file or directory
EXTRA_OEMAKE = " "

do_configure() {
	# Avoid problems with Qt 4.8.0 configure setting QMAKE_LINK from LD (since we want the linker to be g++)
	unset LD

	(echo o; echo yes) | ./configure ${EXTRA_OECONF} || die "Configuring qt failed. EXTRA_OECONF was ${EXTRA_OECONF}"
}

TOBUILD = "\
  src/tools/moc \
  src/corelib \
  src/sql \
  src/xml \
  src/network \
  src/tools/uic \
  src/tools/rcc \
  src/xmlpatterns \
  src/dbus \
  src/gui \
  src/testlib \
  src/qt3support \
  src/tools/uic3 \
  tools/linguist/lrelease \
  tools/linguist/lupdate \
  tools/qdbus/qdbuscpp2xml \
  tools/qdbus/qdbusxml2cpp \
"

do_compile() {
	for i in ${TOBUILD}; do
		cd ${S}/$i && oe_runmake CC="${CC}" CXX="${CXX}"
	done
}

do_install() {
	install -d ${D}${bindir}/
	install -m 0755 bin/qmake ${D}${bindir}/qmake2
	for i in moc uic uic3 rcc lrelease lupdate qdbuscpp2xml qdbusxml2cpp; do
		install -m 0755 bin/${i} ${D}${bindir}/${i}4
	done
    
	install -d ${D}${datadir}/qt4/
	cp -PfR mkspecs ${D}${datadir}/qt4/
	ln -sf linux-g++ ${D}${datadir}/qt4/mkspecs/${BUILD_OS}-oe-g++
	if [ -f ${D}${datadir}/qt4/mkspecs/common/g++-unix.conf ] ; then
		# mkspecs were refactored for 4.8.0
		cp -f ${WORKDIR}/g++.conf ${D}${datadir}/qt4/mkspecs/common/g++-unix.conf
	else
		cp -f ${WORKDIR}/g++.conf ${D}${datadir}/qt4/mkspecs/common/g++.conf
	fi
	cp -f ${WORKDIR}/linux.conf ${D}${datadir}/qt4/mkspecs/common/

	install -m 0644 tools/porting/src/q3porting.xml ${D}${datadir}/qt4/

	for i in ${TOBUILD}; do
		cd ${S}/$i && oe_runmake install INSTALL_ROOT=${D}
	done
}
