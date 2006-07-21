SECTION = "console/utils"
SRC_URI = "http://ftp.info-zip.org/pub/infozip/src/zip231.tar.gz"
LICENSE = "Info-ZIP"
EXTRA_OEMAKE = "'CC=${CC}' 'BIND=${CC}' 'AS=${CC} -c' 'CPP=${CPP}' \
		'CFLAGS=-I. -DUNIX ${CFLAGS}' 'INSTALL=install' \
		'BINFLAGS=0755' 'INSTALL_D=install -d'"

do_compile() {
	oe_runmake -f unix/Makefile generic
}

do_install() {
	oe_runmake -f unix/Makefile prefix=${D}${prefix} \
		   BINDIR=${D}${bindir} MANDIR=${D}${mandir}/man1 \
		   install
}

