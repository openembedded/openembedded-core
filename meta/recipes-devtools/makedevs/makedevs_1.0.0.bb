DESCRIPTION = "A tool to make device nodes"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
SECTION = "base"
SRC_URI = "file://makedevs.c \
           file://COPYING.patch"
PR = "r7"

do_configure() {
	install -m 0644 ${WORKDIR}/makedevs.c ${S}/
}

do_compile() {
	${CC} ${CFLAGS} -o ${S}/makedevs ${S}/makedevs.c
}

do_install() {
	install -d ${D}${base_sbindir}
	install -m 0755 ${S}/makedevs ${D}${base_sbindir}/makedevs
}

BBCLASSEXTEND = "native"
