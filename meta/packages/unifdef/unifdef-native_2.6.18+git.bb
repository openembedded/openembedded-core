DESCRIPTION = "Kernel header preprocessor"
SECTION = "devel"
LICENSE = "GPL"

SRC_URI = "file://unifdef.c"

inherit native

do_compile() {
	${CC} ${CFLAGS} ${LDFLAGS} -o unifdef ${WORKDIR}/unifdef.c
}

NATIVE_INSTALL_WORKS = "1"

do_install() {
	install -m 0755 unifdef ${STAGING_BINDIR}
}

