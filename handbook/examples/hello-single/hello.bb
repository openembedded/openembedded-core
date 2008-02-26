DESCRIPTION = "Simple helloworld application"
SECTION = "examples"
LICENSE = "MIT"

SRC_URI = "file://helloworld.c"

S = "${WORKDIR}"

do_compile() {
	${CC} helloworld.c -o helloworld
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 helloworld ${D}${bindir}
}
