DESCRIPTION = "Kernel header preprocessor"
SECTION = "devel"
LICENSE = "GPL"

SRC_URI = "file://unifdef.c"

inherit native

do_compile() {
	${CC} ${CFLAGS} ${LDFLAGS} -o unifdef ${WORKDIR}/unifdef.c
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 unifdef ${D}${bindir}
}

