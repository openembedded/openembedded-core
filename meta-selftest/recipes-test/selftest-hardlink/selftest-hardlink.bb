LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

LICENSE = "MIT"

SRC_URI = "file://hello.c"

S = "${WORKDIR}"

do_compile () {
	${CC} hello.c -o hello ${CFLAGS} ${LDFLAGS}
}

do_install () {
	install -d ${D}${bindir}
	install -m 755 hello ${D}${bindir}/hello
	ln ${D}${bindir}/hello ${D}${bindir}/hello2
	ln ${D}${bindir}/hello ${D}${bindir}/hello3
	ln ${D}${bindir}/hello ${D}${bindir}/hello4
	dd if=/dev/zero of=${D}${bindir}/sparsetest bs=1 count=0 seek=1M
}
