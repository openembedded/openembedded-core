DESCRIPTION = "TrollTech Makefile Generator"
PRIORITY = "optional"
HOMEPAGE = "http://www.trolltech.com"
SECTION = "devel"
LICENSE = "GPL"
PR = "r1"

QTVER = "qtopia-core-opensource-src-4.3.2"

SRC_URI = "ftp://ftp.trolltech.com/pub/qt/source/${QTVER}.tar.gz \
           file://0001-fix-mkspecs.patch;patch=1 \
           file://use-lflags-last.patch;patch=1 \
           file://linux-oe-qmake.conf"
S = "${WORKDIR}/${QTVER}"

# we need the real target system here
CROSS_SYS := "${TARGET_SYS}"
CROSS_BINDIR := "${STAGING_BINDIR_CROSS}"
inherit autotools cross

export QTDIR = "${S}"
EXTRA_OEMAKE = "-e"

do_configure() {
        # Install the OE build templates
        for template in linux-oe-g++ linux-uclibc-oe-g++ linux-gnueabi-oe-g++
        do
                install -d ${S}/mkspecs/$template
                install -m 0644 ${WORKDIR}/linux-oe-qmake.conf ${S}/mkspecs/$template/qmake.conf
                ln -sf ../linux-g++/qplatformdefs.h ${S}/mkspecs/$template/qplatformdefs.h
        done

	QMAKESPEC=
	PLATFORM=${HOST_OS}-oe-g++
	export PLATFORM
	# yes, TARGET_SYS is correct, because this is a 'cross'-qmake-native :) :M:
	export OE_QMAKE_CC="${CC}"
	export OE_QMAKE_CFLAGS="${CFLAGS}"
	export OE_QMAKE_CXX="${CXX}"
	export OE_QMAKE_CXXFLAGS="-fno-exceptions -fno-rtti ${CXXFLAGS}"
	export OE_QMAKE_LDFLAGS="${LDFLAGS}"
	export OE_QMAKE_LINK="${CCLD}"
	export OE_QMAKE_AR="${AR}"
	export OE_QMAKE_STRIP="${STRIP}"
	export OE_QMAKE_UIC="${STAGING_BINDIR_NATIVE}/uic"
	export OE_QMAKE_MOC="${STAGING_BINDIR_NATIVE}/moc"
	export OE_QMAKE_RCC="non-existant"
	export OE_QMAKE_QMAKE="${STAGING_BINDIR_NATIVE}/qmake"
	export OE_QMAKE_RPATH="-Wl,-rpath-link,"
	echo yes | ./configure -prefix ${STAGING_DIR}/${CROSS_SYS}/qt4 ${EXTRA_OECONF} || die "Configuring qt failed"
}

do_compile() {
	:
}

do_install() {
	install -d ${D}${CROSS_BINDIR}/
	install -m 0755 bin/qmake ${D}${CROSS_BINDIR}/qmake2
	install -m 0755 bin/qmake ${D}${CROSS_BINDIR}/qmake-qt4
	install -d ${D}${STAGING_DIR}/${CROSS_SYS}/qt4/
	cp -PfR mkspecs ${D}${STAGING_DIR}/${CROSS_SYS}/qt4/
	install -d ${D}${STAGING_DIR}/${HOST_SYS}/qt4/
	cp -PfR mkspecs ${D}${STAGING_DIR}/${HOST_SYS}/qt4/
}

sysroot_stage_all_append() {
	sysroot_stage_dir ${D}/${STAGING_DIR_NATIVE}/qt4 ${SYSROOT_DESTDIR}/${STAGING_DIR_NATIVE}/qt4
	sysroot_stage_dir ${D}/${STAGING_DIR_TARGET}/qt4 ${SYSROOT_DESTDIR}/${STAGING_DIR_TARGET}/qt4
}


