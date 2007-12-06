DESCRIPTION = "User-Interface-, Meta-Object-, and Resource Compiler for Qt/[X11|Mac|Embedded] version 4.x"
DEPENDS = "zlib-native"
SECTION = "libs"
HOMEPAGE = "http://www.trolltech.com"
PRIORITY = "optional"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "ftp://ftp.trolltech.com/qt/source/qtopia-core-opensource-src-${PV}.tar.gz"
S = "${WORKDIR}/qtopia-core-opensource-src-${PV}"

inherit native

EXTRA_OECONF = "-prefix ${STAGING_DIR_NATIVE}/qt4 \
                -qt-libjpeg -qt-gif -system-zlib \
                -no-nis -no-cups -no-exceptions  \
                -no-accessibility -no-libjpeg    \
                -no-nas-sound -no-sm             \
                -no-xshape    -no-xinerama       \
                -no-xcursor   -no-xrandr         \
                -no-xrender   -no-fontconfig     \
                -no-tablet    -no-xkb            \
                -no-libpng                       \
                -verbose -release  -fast -static \
                -qt3support "
# yank default -e
EXTRA_OEMAKE = " "

do_configure() {
	sed -i 's:^QT += xml qt3support$:QT += xml qt3support network:' "${S}"/src/tools/uic3/uic3.pro
	echo yes | ./configure ${EXTRA_OECONF} || die "Configuring qt failed. EXTRA_OECONF was ${EXTRA_OECONF}"
}

TOBUILD = "\
  src/tools/moc \
  src/corelib \
  src/sql \
  src/qt3support \
  src/xml \
  src/tools/uic \
  src/tools/rcc \
  src/network \
  src/gui \
  src/tools/uic3 \
"

do_compile() {
	unset CC CXX CFLAGS LFLAGS CXXFLAGS CPPFLAGS
	for i in ${TOBUILD}; do
		cd ${S}/$i && oe_runmake CC="${CC}" CXX="${CXX}"
	done
}

do_stage() {
        install -m 0755 bin/moc ${STAGING_BINDIR}/moc4
        install -m 0755 bin/uic ${STAGING_BINDIR}/uic4
        install -m 0755 bin/uic3 ${STAGING_BINDIR}/uic34
        install -m 0755 bin/rcc ${STAGING_BINDIR}/rcc4
        install -d ${STAGING_DIR_NATIVE}/qt4/
        install -m 0644 tools/porting/src/q3porting.xml ${STAGING_DIR_NATIVE}/qt4/
}
